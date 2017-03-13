<?php
//header("Content-type: text/html; charset=utf-8"); 

/*
 * 返回需要的news列表信息
 */

require 'mysql_connect.php';

$obj = file_get_contents('php://input');

$obj = json_decode($obj);

$limit = $obj->limit;
$offset = $obj->offset;
$news_type = $obj->news_type;
$finder_id = $obj->finder_id;

$sql = "";

//TODO 为啥  $news_type不加单引号识别不了

//未登录用户
if ($news_type == "Blank") {
    $sql = "select * from news order by news_id desc limit $limit offset $offset"; //SQL
} else {
    $sql = "select * from news where type = '$news_type' order by news_id desc limit $limit offset $offset"; //SQL
}

$result = mysql_query($sql);//执行SQL

$json = "";
$data = array(); //定义好一个数组.PHP中array相当于一个数据字典.
//定义一个类,用到存放从数据库中取出的数据.

class News
{
    public $news_id;
    public $title;
    public $desc;
    public $time;
    public $content_url;
    public $pic_url;
    public $type;
    public $finder;
    public $isBooked;//是否是加入书签的
    public $isLike;//是否是喜欢的
}

while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {

    //未登录
    if($finder_id == 0){
        $user = new News();
        $user->news_id = $row["news_id"];
        $user->title = $row["title"];
        $user->desc = $row["desc"];
        $user->time = $row["time"];
        $user->content_url = $row["content_url"];
        $user->pic_url = $row["pic_url"];
        $user->type = $row["type"];


        $result_finder = mysql_query("select * from finder WHERE finder_id = '" . $row["finder_id"] . "'");
        while($r_finder = mysql_fetch_array($result_finder))
        {
            $user->finder = $r_finder["name"];
        }
        $user->isBooked = false;
        $user->isLike = false;

        $data[] = $user;
    }else{

        $result_finder_like = mysql_query("select * from finder_like WHERE news_id = '" . $row["news_id"] . "' AND finder_id = '$finder_id'");
        $r_like = mysql_fetch_row($result_finder_like);
        $r_like = $r_like[0];

        $sql_finder_book = mysql_query("select * from finder_bookmark WHERE news_id = '" . $row["news_id"] . "' AND finder_id = '$finder_id'");
        $r_book = mysql_fetch_row($sql_finder_book);
        $r_book = $r_book[0];

        $user = new News();

        if($r_book != 0){
            $user->isBooked = true;
        }else{
            $user->isBooked = false;
        }

        if($r_like != 0){
            $user->isLike = true;
        }else{
            $user->isLike = false;
        }

        $user->news_id = $row["news_id"];
        $user->title = $row["title"];
        $user->desc = $row["desc"];
        $user->time = $row["time"];
        $user->content_url = $row["content_url"];
        $user->pic_url = $row["pic_url"];
        $user->type = $row["type"];
        //$user->finder = $row["finder_id"];

        $result_finder = mysql_query("select * from finder WHERE finder_id = '" . $row["finder_id"] . "'");

        while($r_finder = mysql_fetch_array($result_finder))
        {
            $user->finder = $r_finder["name"];
        }

        $data[] = $user;
    }

}

$json = json_encode($data);//把数据转换为JSON数据.
echo "{" . '"news"' . ":" . $json . "}";


?>