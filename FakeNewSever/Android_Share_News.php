<?php
header("Content-type: text/html; charset=utf-8"); 

require 'mysql_connect.php';

$title = $_POST['title'];
$desc = $_POST['desc'];
$content_url = $_POST['news_link'];
$pic_url = $_POST['news_img_link'];
$finder_id = $_POST['finder_id'];

$tags_name = $_POST['tags_name'];

$tags_id = "";
$type_id = "";


$pre = mysql_query("select * from tags where tags_name = '$tags_name' ");
$number = mysql_num_rows($pre);

if($number != 0){
    while($row = mysql_fetch_array($pre))
    {
        $tags_id = $row['tags_id'];
        $type_id = $row['type_id'];
    }
}

$sql="insert into `news`(`title`,`desc`,`content_url`,`pic_url`,`finder_id`,`type_id`,`tags_id`)values('$title','$desc','$content_url','$pic_url', '$finder_id','$type_id','$tags_id')";

$set=mysql_query($sql);

if($set)
{
	//echo "insert success";
	echo "200";
}else{
	echo "插入错误".$title;
}
?>