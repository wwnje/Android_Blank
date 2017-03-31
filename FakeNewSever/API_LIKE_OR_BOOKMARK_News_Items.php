<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/3/12
 * Time: 下午2:32
 * 喜欢还是加入书签
 */

require 'mysql_connect.php';

$items_id = $_POST['news_id'];
$finder_id = $_POST['finder_id'];
$subType = $_POST['subType'];
$sub = $_POST['sub'];//添加还是删除 true or false

$sql ="";
$_echo = "";


if($subType == "bookmark"){//订阅新闻

    $q = mysql_query("select count(*) from finder_bookmark where news_id = '$items_id' AND finder_id = '$finder_id'");
    $r = mysql_fetch_row($q);
    $r = $r[0];

    if($r == 0 && $sub == "true"){
        //没有订阅过并且是订阅操作
        $sql = "insert into finder_bookmark (news_id, finder_id) values('$items_id', '$finder_id') ";
        $_echo = "添加书签成功！";
        echo $_echo;
    }else if ($r > 0 && $sub == "false"){
        $sql = "delete from finder_bookmark where news_id = '$items_id' AND finder_id = '$finder_id'";//删除取消
        $_echo = "删除书签！";
        echo $_echo;
    }
    else if ($r == 0 && $sub == "false"){
        //没有这个数据
        $_echo = "没有这条数据 无法删除书签";
        echo $_echo;
    }else if ($r > 0 && $sub == "true"){
        $_echo = "不能重复添加书签！";
        echo $_echo;
    }

}elseif($subType == "like"){//订阅标签

    $q = mysql_query("select count(*) from finder_like where news_id = '$items_id' AND finder_id = '$finder_id'");
    $r = mysql_fetch_row($q);
    $r = $r[0];

    if($r == 0 && $sub == "true"){
        //没有订阅过并且是订阅操作
        $sql = "insert into finder_like (news_id, finder_id) values('$items_id', '$finder_id') ";
        $_echo = "添加喜欢成功！";
        echo $_echo;
    }else if ($r > 0 && $sub == "false"){
        $sql = "delete from finder_like where news_id = '$items_id' AND finder_id = '$finder_id'";//删除取消
        $_echo = "删除喜欢！";
        echo $_echo;
    }
    else if ($r == 0 && $sub == "false"){
        //没有这个数据
        $_echo = "没有这条数据 无法删除喜欢";
        echo $_echo;
    }else if ($r > 0 && $sub == "true"){
        $_echo = "不能重复喜欢！";
        echo $_echo;
    }else{
        $_echo = "错误！";
        echo $_echo;
    }
}else{
    echo "未知错误！" .$subType;
}

if (!mysql_query($sql, $con)) {
    echo $_echo ."添加喜欢失败！" . mysql_error();
} else {
    echo $_echo;
}

?>