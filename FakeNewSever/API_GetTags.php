<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 返回所有Tags的列表
 * tags_id
 * tags_name
 */

 require 'mysql_connect.php';


$obj = file_get_contents('php://input');

$obj=json_decode($obj);

$limit = $obj->limit;
$offset = $obj ->offset;

//这里出错了
$sql ="select * from tags limit $limit offset $offset"; //SQL


$result =mysql_query($sql);//执行SQL


$json ="";
$data =array(); //定义好一个数组.PHP中array相当于一个数据字典.

//定义一个类,用到存放从数据库中取出的数据.

//所有订阅标签
class Tags
{
    public $tags_id ;
    public $tags_name ;
}

//
while ($row= mysql_fetch_array($result, MYSQL_ASSOC))
{
    $tags =new Tags();
    $tags->tags_id = $row["tags_id"];
    $tags->tags_name = $row["tags_name"];
    $data[]=$tags;
}
$json = json_encode($data);//把数据转换为JSON数据.
echo "{".'"tags"'.":".$json."}";



?>