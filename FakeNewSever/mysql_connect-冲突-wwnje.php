<?php

	$con = mysql_connect("localhost", "root", "mysqladmin");
	//设置字符集为utf8
	mysql_query("SET NAMES 'utf8'");
	mysql_query("SET CHARACTER SET utf8");
	mysql_query("SET CHARACTER_SET_RESULT=utf8");
	
	if (!$con){
		die(mysql_error());
	}
	
	mysql_select_db("news", $con);
?>