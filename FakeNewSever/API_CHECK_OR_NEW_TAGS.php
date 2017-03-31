<?php
/**
 * 新建标签
 */
header("Content-type: text/html; charset=utf-8"); 

require 'mysql_connect.php';

	$tags_name = $_POST['item_name'];//标签名字
    $isCheck = $_POST['isCheck'];//标签名字

    $type_id = "";//类型

    if($isCheck == "false"){
        $type_id = $_POST['type_id'];//类型
    }


	$q = mysql_query("select count(*) from tags where tags_name = '$tags_name'");
	$r = mysql_fetch_row($q);
	$r = $r[0];

    //如果没有此人
	if($r == 0){
        if($isCheck == "true"){
            echo "404_NOT_FOUND";
        }else{
            $sql = "insert into tags (tags_name, type_id) values('$tags_name', '$type_id') ";

            if(!mysql_query($sql, $con)){
                echo "201_ERROR".mysql_error();//添加失败
            } else {
                get_tags_id($tags_name);
            }
        }
	}else{
        get_tags_id($tags_name);//存在
	}

	function get_tags_id($tags_name){
        $tags_id = "";

        $pre = mysql_query("select * from tags where tags_name = '$tags_name' ");
        while($row = mysql_fetch_array($pre))
        {
            $tags_id = $row['tags_id'];
        }
        echo $tags_id;
    }
?>