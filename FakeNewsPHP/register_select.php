<?php
/*
* ��½�ж�
*/
require 'config.php';

header("Content-type:text/html;charset=utf-8");

$username=$_POST['username'];
$password=md5($_POST['password']);
$md=$_POST['md'];

if($md == 'orvnge'){
	//����
	$sql="insert into `login`(`username`,`password`)values('$username','$password')";
	$set=mysql_query($sql);
}else{
	echo "<script>alert('ע���� error');location='index.php';</script>";
}


if($set)
{
	echo "register success";
	//echo "<script>alert('login success');location='main.php';</script>";
	//�ض��������
	header("Location: index.php");
//ȷ���ض���󣬺������벻�ᱻִ��
	exit;

}else{
	echo "register error";
}
?>
