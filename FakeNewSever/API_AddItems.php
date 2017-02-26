<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/2/19
 * Time: 下午6:33
 * 订阅tags或者news
 */

require 'mysql_connect.php';


$items_id = $_POST['items_id'];
$finder_id = $_POST['finder_id'];
$subType = $_POST['subType'];
$sub = $_POST['sub'];//订阅还是取消订阅 true or false

$sql ="";
$_echo = "";



if($subType == "news"){//订阅新闻

    $q = mysql_query("select count(*) from finder_type where type_id = '$items_id' AND finder_id = '$finder_id'");
    $r = mysql_fetch_row($q);
    $r = $r[0];

    if($r == 0 && $sub == "true"){
        //没有订阅过并且是订阅操作
        $sql = "insert into finder_type (type_id, finder_id) values('$items_id', '$finder_id') ";
        $_echo = "订阅成功！";
        echo $_echo;
    }else if ($r > 0 && $sub == "false"){
        $sql = "delete from finder_type where type_id = '$items_id' AND finder_id = '$finder_id'";//删除取消
        $_echo = "取消订阅news！";
        echo $_echo;
    }
    else if ($r == 0 && $sub == "false"){
        //没有这个数据
        $_echo = "没有这条数据 无法取消订阅";
        echo $_echo;
    }else if ($r > 0 && $sub == "true"){
        $_echo = "不能重复订阅news！";
        echo $_echo;
    }

}elseif($subType == "tags"){//订阅标签

    $q = mysql_query("select count(*) from finder_tags where tags_id = '$items_id' AND finder_id = '$finder_id'");
    $r = mysql_fetch_row($q);
    $r = $r[0];

    if($r == 0 && $sub == "true"){
        //没有订阅过并且是订阅操作
        $sql = "insert into finder_tags (tags_id, finder_id) values('$items_id', '$finder_id') ";
        $_echo = "订阅tags成功！";
        echo $_echo;
    }else if ($r > 0 && $sub == "false"){
        $sql = "delete from finder_tags where tags_id = '$items_id' AND finder_id = '$finder_id'";//删除取消
        $_echo = "取消订阅tags！";
        echo $_echo;
    }
    else if ($r == 0 && $sub == "false"){
        //没有这个数据
        $_echo = "没有这条数据 无法取消订阅";
        echo $_echo;
    }else if ($r > 0 && $sub == "true"){
        $_echo = "不能重复订阅news！";
        echo $_echo;
    }


    if($r == 0){
    }else{
        $_echo = "不能重复订阅tags！";
        echo $_echo;
    }

}else{
    echo "未知错误！" .$subType;
}

if (!mysql_query($sql, $con)) {
    echo $_echo ."订阅失败！" . mysql_error();
} else {
    echo $_echo;
}


//
//}else{
//    echo "注册失败！已有此账号";
//}

?>