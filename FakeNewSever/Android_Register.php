<?php
header("Content-type: text/html; charset=utf-8"); 

require 'mysql_connect.php';

	$name = $_POST['username'];
	
	$password = $_POST['password'];

	//$sql = "insert into user (name, password) values('我heni', '222') ";
	//$query = mysql_query($sql, $con);


	$q = mysql_query("select count(*) from finder where name = '$name'");
	$r = mysql_fetch_row($q);
	$r = $r[0];

	if($r == 0){
		$sql = "insert into finder (name, password) values('$name', '$password') ";

		if(!mysql_query($sql, $con)){
			echo "注册失败！".mysql_error();
		} else {
			echo "注册成功！";
			echo $name;
			echo $password;
		}

	}else{
		echo "注册失败！已有此账号";
	}


?>