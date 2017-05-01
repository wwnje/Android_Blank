<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 返回所有书签
 * bookmark_id,
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


class Finder_News
{
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
}

//获得该finder的所有 bookmark_id, news_id, time;
$sql1 = "select * from news WHERE finder_id = $finder_id limit $limit offset $offset"; //SQL
$result1 = mysql_query($sql1);//执行SQL

while ($row1 = mysql_fetch_assoc($result1))//将result结果集中查询结果取出一条
{
    $finder_news = new Finder_News();

    $finder_news->news_id = $row1["news_id"];
    $finder_news->news_title = $row1["title"];
    $finder_news->news_desc = $row1["desc"];
    $finder_news->news_content_url = $row1["content_url"];
    $finder_news->news_pic_url = $row1["pic_url"];

    $result_type = mysql_query("select * from type WHERE type_id = '" . $row1["type_id"] . "'");
    while ($r_type = mysql_fetch_array($result_type)) {
        $finder_news->type_name = $r_type["type_name"];
    }

    $finder_news->finder_id = $row1["finder_id"];

    $finder_news->type_id = $row1["type_id"];
    $finder_news->tags_id = $row1["tags_id"];

    $data[] = $finder_news;
}
$json = json_encode($data);//把数据转换为JSON数据.

echo "{" . '"my_news"' . ":" . $json . "}";

//echo "{\"type\":[{\"type_id\":\"1\",\"type_name\":\"World\",\"type_version\":\"0\"},{\"type_id\":\"2\",\"type_name\":\"Life\",\"type_version\":\"0\"},{\"type_id\":\"3\",\"type_name\":\"Game\",\"type_version\":\"0\"},{\"type_id\":\"4\",\"type_name\":\"Code\",\"type_version\":\"0\"},{\"type_id\":\"5\",\"type_name\":\"Design\",\"type_version\":\"0\"},{\"type_id\":\"6\",\"type_name\":\"Book\",\"type_version\":\"0\"},{\"type_id\":\"7\",\"type_name\":\"Movie\",\"type_version\":\"0\"},{\"type_id\":\"8\",\"type_name\":\"Arts\",\"type_version\":\"0\"},{\"type_id\":\"9\",\"type_name\":\"Music\",\"type_version\":\"0\"},{\"type_id\":\"10\",\"type_name\":\"Technology\",\"type_version\":\"0\"},{\"type_id\":\"11\",\"type_name\":\"Sports\",\"type_version\":\"0\"},{\"type_id\":\"12\",\"type_name\":\"Fun\",\"type_version\":\"0\"}]}";

?>



