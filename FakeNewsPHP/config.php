<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2016/6/27
 * Time: 20:52
 * 数据库连接
 */

header("Content-type:text/html;charset=utf-8");

$con=mysql_connect("","","");
//设置字符集为utf8
mysql_query("SET NAMES 'utf8'");
mysql_query("SET CHARACTER SET utf8");
mysql_query("SET CHARACTER_SET_RESULT=utf8");

if(!$con)
{
    echo mysql_error();
    //输出错误
}

mysql_select_db("",$con);
//连接数据库


//$password=md5("123");
//增加
//$sql="insert into `login`(`username`,`password`)values('admin','$password')";
//删除
//$sql="delete from `login` where `id` = '2'";
//修改
//$sql="update `login` set `username`='users' where id='3'";
//查询
//$sql="select `password` from `login` where `username`='admin'";

//$set=mysql_query($sql);


?>