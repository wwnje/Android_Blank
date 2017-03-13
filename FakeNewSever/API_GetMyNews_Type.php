<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 返回所有我订阅的新闻类型列表
 */
require 'mysql_connect.php';

$obj = file_get_contents('php://input');
$obj = json_decode($obj);

$finder_id = $obj->finder_id;

$json ="";
$data =array(); //定义好一个数组.PHP中array相当于一个数据字典.

//定义一个类,用到存放从数据库中取出的数据.
//所有订阅标签
class MyType
{
    public $type_id ;
    public $type_name ;
}

$sql1 = "select * from finder_type WHERE finder_id = $finder_id";
$result1 = mysql_query($sql1);//执行SQL

while ($row1 = mysql_fetch_assoc($result1))//将result结果集中查询结果取出一条
{
    //获取id 来找name
    $sql = "select * from type WHERE type_id = '" . $row1['type_id'] . "'"; //SQL

    $result = mysql_query($sql);//执行SQL
    while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
        $myType = new MyType();
        $myType->type_id = $row["type_id"];
        $myType->type_name = $row["type_name"];
        $data[] = $myType;
    }
}

$json = json_encode($data);//把数据转换为JSON数据.
echo "{".'"myTypes"'.":".$json."}";

?>