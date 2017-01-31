<?php
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2017/1/31
 * Time: 21:46
 *用户添加书签
 */

header("Content-type: text/html; charset=utf-8");
require 'mysql_connect.php';

	$finder_id = $_POST['finder_id'];
	$news_id = $_POST['news_id'];

//	$q = mysql_query("select count(*) from finder_bookmark where news_id = '$news_id' AND finder_id = '$finder_id'");
//	$r = mysql_fetch_row($q);
//	$r = $r[0];
//
//	if($r == 0){

        $sql = "insert into finder_bookmark (news_id, finder_id) values('$news_id', '$finder_id') ";

        if(!mysql_query($sql, $con)){
            echo "添加书签失败！".mysql_error();
        } else {
            echo "添加书签成功！";
            echo $finder_id;
            echo $news_id;
        }
//
//    }else{
//        echo "注册失败！已有此账号";
//    }


?>