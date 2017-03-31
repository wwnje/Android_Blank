<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 返回喜欢的
 */

require 'mysql_connect.php';


$obj = file_get_contents('php://input');
$obj = json_decode($obj);

/**
 * 请求数据
 */
$limit = $obj->limit;
$offset = $obj->offset;
$finder_id = $obj->finder_id;


$json = "";
$data = array(); //定义好一个数组.PHP中array相当于一个数据字典.


class Like
{
    public $like_id;//书签号  用于之后删除
    public $news_id;
    /**
     * @var 内容本身信息
     */
    public $news_title;
    public $news_desc;
    public $news_content_url;
    public $news_pic_url;
    public $type_name;//暂时定一个大类  数量最多的
    public $tags_id;
    public $type_id;
    /**
     * @var finder信息 用于好友关系
     */
    public $finder_id;//推荐的人id
    public $finder_name;//推荐人的姓名

}

//获得该finder的所有 bookmark_id, news_id, time;
$sql1 = "select * from finder_like WHERE finder_id = $finder_id limit $limit offset $offset"; //SQL
$result1 = mysql_query($sql1);//执行SQL

while ($row1 = mysql_fetch_assoc($result1))//将result结果集中查询结果取出一条
{
    $like_id = $row1["like_id"];

    $sql = "select * from news WHERE news_id = '" . $row1['news_id'] . "'"; //查找该条news的信息还有推荐人的id
    $result = mysql_query($sql);//执行SQL

    while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {

        $book = new Like();

        $book->like_id = $like_id;
        $book->news_id = $row["news_id"];
        $book->news_title = $row["title"];
        $book->news_desc = $row["desc"];
        $book->news_content_url = $row["content_url"];
        $book->news_pic_url = $row["pic_url"];
        $book->finder_id = $row["finder_id"];

        $result_type = mysql_query("select * from type WHERE type_id = '" . $row["type_id"] . "'");
        while($r_type = mysql_fetch_array($result_type))
        {
            $book->type_name = $r_type["type_name"];
        }

        $book->type_id = $row["type_id"];
        $book->tags_id = $row["tags_id"];

        //查找推荐人的信息
        $sql_finder = "select name from finder WHERE finder_id = '" . $row["finder_id"] . "'"; //查找finder
        $result_finder = mysql_query($sql_finder);//执行SQL

        while ($row_finder = mysql_fetch_array($result_finder, MYSQL_ASSOC)) {
            $book->finder_name = $row_finder["name"];
        }
        $data[] = $book;
    }
}
$json = json_encode($data);//把数据转换为JSON数据.

echo "{".'"likelists"'.":".$json."}";

//echo "{\"type\":[{\"type_id\":\"1\",\"type_name\":\"World\",\"type_version\":\"0\"},{\"type_id\":\"2\",\"type_name\":\"Life\",\"type_version\":\"0\"},{\"type_id\":\"3\",\"type_name\":\"Game\",\"type_version\":\"0\"},{\"type_id\":\"4\",\"type_name\":\"Code\",\"type_version\":\"0\"},{\"type_id\":\"5\",\"type_name\":\"Design\",\"type_version\":\"0\"},{\"type_id\":\"6\",\"type_name\":\"Book\",\"type_version\":\"0\"},{\"type_id\":\"7\",\"type_name\":\"Movie\",\"type_version\":\"0\"},{\"type_id\":\"8\",\"type_name\":\"Arts\",\"type_version\":\"0\"},{\"type_id\":\"9\",\"type_name\":\"Music\",\"type_version\":\"0\"},{\"type_id\":\"10\",\"type_name\":\"Technology\",\"type_version\":\"0\"},{\"type_id\":\"11\",\"type_name\":\"Sports\",\"type_version\":\"0\"},{\"type_id\":\"12\",\"type_name\":\"Fun\",\"type_version\":\"0\"}]}";

?>



