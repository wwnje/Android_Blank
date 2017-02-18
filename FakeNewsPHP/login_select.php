<?php
/*
* 登陆判断
*/
require 'config.php';

//开启以便正常赋值
session_start(); //开启session


$username=$_POST['username'];
$password=md5($_POST['password']);


//查询
//$sql="select * from `login` where `user`='".$username."' and `password`='".$password."'";
$sql="select `password` from `login` where `username` = '".$username."'";

$set=mysql_query($sql);
$result=mysql_fetch_array($set);

if($result['password']==$password)
{
	$_SESSION['login']=$username;

	echo "login success";
	//echo "<script>alert('login success');location='main.php';</script>";
	//重定向浏览器
	header("Location: index.php");
//确保重定向后，后续代码不会被执行
	exit;

}else{
	echo "login error";
}
?>

