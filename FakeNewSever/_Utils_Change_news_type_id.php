<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/3/31
 * Time: 下午2:24
 * 修改news name到id
 */
header("Content-type: text/html; charset=utf-8");
require 'mysql_connect.php';


$pre = mysql_query("select * from news");
$number = mysql_num_rows($pre);


if ($number != 0) {
    echo "一共" . $number . "条语句";

//    while ($row = mysql_fetch_array($pre)) {
//        if ($row['type'] == "Blank" || $row['type'] == "FakeNews") {
//            mysql_query("UPDATE news SET type_id = '0' WHERE FirstName = 'Peter' AND LastName = 'Griffin'");
//            echo $row['type'];
//        } else {
//            //echo $row['type'];
//        }
//    }

    //无用 type_id = 1 NONE   tags_id = 1 NONE
    mysql_query("UPDATE news SET type_id = '1', tags_id = '1' WHERE type = 'Blank' OR type = 'FakeNews'");

    mysql_query("UPDATE news SET type_id = '2', tags_id = '1' WHERE type = 'Life'");
    mysql_query("UPDATE news SET type_id = '3', tags_id = '1' WHERE type = 'Game'");
    mysql_query("UPDATE news SET type_id = '4', tags_id = '1' WHERE type = 'Code'");
    mysql_query("UPDATE news SET type_id = '5', tags_id = '1' WHERE type = 'Design'");
    mysql_query("UPDATE news SET type_id = '6', tags_id = '1' WHERE type = 'Book'");
    mysql_query("UPDATE news SET type_id = '7', tags_id = '1' WHERE type = 'Movie'");
    mysql_query("UPDATE news SET type_id = '8', tags_id = '1' WHERE type = 'Arts'");
    mysql_query("UPDATE news SET type_id = '10', tags_id = '1' WHERE type = 'Technology'");
    mysql_query("UPDATE news SET type_id = '11', tags_id = '1' WHERE type = 'Sports'");
    mysql_query("UPDATE news SET type_id = '12', tags_id = '1' WHERE type = 'World'");
    mysql_query("UPDATE news SET type_id = '13', tags_id = '1' WHERE type = 'Story'");
}
?>