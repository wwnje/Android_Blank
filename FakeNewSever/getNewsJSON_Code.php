<?php
//header("Content-type: text/html; charset=utf-8"); 

/*
 * 获得JSON数据
 * 返回值：title desc time content_url pic_url
 */
 
 require 'mysql_connect.php';
 



//$limit = 3;

//$offset = 1;


//$json_string='{"id":1,"name":"foo","email":"foo@foobar.com","interest":["wordpress","php"]} ';

//$json_string='{"limit":"3","offset":"1"} ';

//$json_string=$_POST['obj'];


$obj = file_get_contents('php://input');

$obj=json_decode($obj);

//$return            = array();
//$itemid            = intval($postDdata['itemid']);   


//$json_string='{"offset":"1","limit":"3"}';

//$obj=json_decode($json_string);


$limit = $obj->limit;
$offset = $obj ->offset;



//$limit = $_POST;

//echo $limit;

//$offset = intval($obj['offset']);

//$limit = intval($obj['limit']);



//echo $offset;


	//$sql1 = "insert into user (name, password) values('$_POST[limit]', '$offset') ";

$sql1 = "insert into user (name, password) values('$limit', '$offset') ";

	mysql_query($sql1, $con);


	/*if(!mysql_query($sql1, $con)){
		echo "添加数据失败！".mysql_error();
	} else {
		echo "successful添加数据成功！";
	}*/



$sql ="select * from news where type = 'Code' order by news_id desc limit $limit offset $offset"; //SQL


$result =mysql_query($sql);//执行SQL


$json ="";
$data =array(); //定义好一个数组.PHP中array相当于一个数据字典.
//定义一个类,用到存放从数据库中取出的数据.

class User 
{
public $title ;
public $desc ;
public $time ;
public $content_url ;
public $pic_url ;
public $type ;
    public $finder ;
}

//
while ($row= mysql_fetch_array($result, MYSQL_ASSOC))
{
$user =new User();
$user->title = $row["title"];
$user->desc = $row["desc"];
$user->time = $row["time"];
$user->content_url = $row["content_url"];
$user->pic_url = $row["pic_url"];
$user->type = $row["type"];
    $user->finder = $row["finder_id"];


$data[]=$user;
}
$json = json_encode($data);//把数据转换为JSON数据.
echo "{".'"user"'.":".$json."}";



?>