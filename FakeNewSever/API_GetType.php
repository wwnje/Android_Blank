<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/25
 * Time: 下午3:18
 * 返回新闻类别
 */

require 'mysql_connect.php';


$obj = file_get_contents('php://input');

$obj=json_decode($obj);

$limit = $obj->limit;
$offset = $obj ->offset;
$type_version = $obj ->type_version;


//这里出错了
$sql ="select * from type limit $limit offset $offset"; //SQL
$result =mysql_query($sql);//执行SQL

$json ="";
$data =array(); //定义好一个数组.PHP中array相当于一个数据字典.

//定义一个类,用到存放从数据库中取出的数据.

//所有新闻内容
class Type
{
    public $type_id;
    public $type_name ;
    public $type_version;//申请版本
}

//
while ($row= mysql_fetch_array($result, MYSQL_ASSOC))
{
    $type =new Type();
    $type->type_id = $row["type_id"];
    $type->type_name = $row["type_name"];
    $type->type_version = $type_version;
    $data[]=$type;
}
$json = json_encode($data);//把数据转换为JSON数据.
echo "{".'"type"'.":".$json."}";



?>