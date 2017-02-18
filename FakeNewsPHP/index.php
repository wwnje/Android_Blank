


<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>fakenews</title>
 <link rel="shortcut icon" href="favicon.ico" /> 

</head>

<body>

<?php


header("Content-type:text/html;charset=utf-8");

session_start(); //开启session
    //判断登录时的session是否存在 如果存在则表示已经登录
    if(!$_SESSION['login']){
        echo "none";
    }
	else{
		echo "now:";
		echo $_SESSION['login'];
	}


?>
<br><br>
	<a href = "register.php">register</a>
	<a href = "login.php">login</a>
	<a href = "insert.php">insert</a>
	<a href='login_out.php'>exit</a>;
<center>

    fasdfadsfsadfsadfs
<table border="1" cellspacing="0" width='80%'">
<caption>hello</caption>
<tr><th>编号</th>
<th>title</th>
<th>desc</th>
<th> 链接</th>
<th>图片</th>
<th>type</th></tr>

<?php
require 'config.php';

/*传入页码*/

$page = $_GET['p'];


//LIMIT起始位置，显示条数

$q="select * from news order by id desc LIMIT ".($page - 1)*5 .",5";//设置查询指令

$result=mysql_query($q);//执行查询

while($row=mysql_fetch_assoc($result))//将result结果集中查询结果取出一条
{
 echo"<tr>
 <td>".$row["news_id"]."</td>
 <td>".$row["title"]."</td>
 <td>".$row["desc"]."</td>
 <td>".$row["content_url"]."</td>
 <td>".$row["pic_url"]."</td>
 <td>".$row["type"]."</td>
 <tr>";
}

//释放结果 关闭连接
//mysql_free_result($result);

/*显示分页*/

echo '<tr><td colspan="5">

<a href="index.php?p=1">index</a>  
<a href="index.php?p=' . ($page - 1) . '">forward</a>   
<a href="index.php?p=' . ($page + 1) . '">next</a>  

</td></tr>';

?>
</table>
</center>
</body>
</html>