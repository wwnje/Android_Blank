<?php

//session_start();
/*
*提交输入界面
*/
//require 'config.php';
header("Content-type:text/html;charset=utf-8");
//
//session_start(); //开启session
//    //判断登录时的session是否存在 如果存在则表示已经登录
//    if(!$_SESSION['login']){
//        // !$_SESSION['islogin']  表示不存在 回到登录页面
//        header("Location: login.php");exit;
//    }
	
?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>FakeNews insert</title>
</head>

<body>
<center>
		请输入提交新闻<br />
<table>
<col width=150>
<col width=200>

	<form action="insert_select.php" method="post">
		<tr><td>标题（20字以内）：</td><td><textarea name="title" cols=40 rows=4></textarea></td></tr>
		<tr><td>描述（40字以内）：</td><td><textarea name="desc" cols=40 rows=4></textarea></td></tr>
		<tr><td>文章链接：</td><td><textarea name="content_url" cols=40 rows=4></textarea></td></tr>
		<tr><td>图片链接：</td><td><textarea name="pic_url" cols=40 rows=4></textarea></td></tr>
		<tr><td>
			类型:</td><td>
		<select name="type">
		  <option value ="FakeNews">FakeNews</option>
		  <option value ="World">World</option>
		  <option value="Life">Life</option>
		  <option value="Arts">Arts</option>
		  <option value="Code">Code</option>
		  <option value="Design">Design</option>
		  <option value="Movie">Movie</option>
		  <option value="Technology">Technology</option>
		  <option value="Book">Book</option>
		  <option value="Game">Game</option>
		</select></td></tr>
		<tr><td><input type="submit" name="sub" value="submit"></td></tr>
	</form>

</table>
	
</center>
</body>

</html>