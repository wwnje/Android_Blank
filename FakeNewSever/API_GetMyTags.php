<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 返回所有我订阅的列表
 * tags_id
 * tags_name
 */
require 'mysql_connect.php';

$json = "";
$data = array(); //定义好一个数组.PHP中array相当于一个数据字典.


//定义一个类,用到存放从数据库中取出的数据.
//所有订阅标签
class MyTags
{
    public $tags_id ;
    public $tags_name ;
    public $myTags_version;//申请版本
}

$obj = file_get_contents('php://input');
$obj = json_decode($obj);

$limit = $obj->limit;
$offset = $obj->offset;
$finder_name = $obj->finder_name;
$myTags_version = $obj->myTags_version;//我的标签版本

//$limit = 100;
//$offset = 0;
//$finder_name = '123';

//获得该finder的书签id  注意这里后面 L和O好像是反的
$sql1 = "select * from finder_tags WHERE finder_name = $finder_name limit $limit offset $offset"; //SQL
$result1 = mysql_query($sql1);//执行SQL


while ($row1 = mysql_fetch_assoc($result1))//将result结果集中查询结果取出一条
{
    //获取id 来找name
    $sql = "select * from tags WHERE tags_id = '" . $row1['tags_id'] . "'"; //SQL
    $result = mysql_query($sql);//执行SQL
    while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
        $myTags = new MyTags();
        $myTags->tags_id = $row["tags_id"];
        $myTags->tags_name = $row["tags_name"];
        $myTags->myTags_version = $myTags_version;

        $data[] = $myTags;
    }
}

$json = json_encode($data);//把数据转换为JSON数据.
echo "{" . '"tags"' . ":" . $json . "}";


?>