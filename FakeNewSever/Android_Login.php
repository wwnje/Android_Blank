<?php
header("Content-type: text/html; charset=utf-8");

require 'mysql_connect.php';

$name = $_POST['username'];
$password = $_POST['password'];

$q = mysql_query("select * from finder where name = '$name' ");
$number = mysql_num_rows($q);

if($number != 0){
    while($row = mysql_fetch_array($q))
    {
        if($password == $row['password']){
            echo $row['finder_id'];//登录成功 返回用户id

        }else{
            echo "-1";//密码错误
        }
    }
}

else{
    echo "0";//没有这个账号
}

?>