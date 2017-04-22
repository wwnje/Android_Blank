<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/4/10
 * Time: 下午12:27
 * 计算power
 */

require 'mysql_connect.php';
date_default_timezone_set('PRC');//设置时间

/**
 * 计算power
 * 1.根据id找该用户
 * 2.找出信息
 * 3.根据时间等信息 更新该用户power
 **/

SetPower(1);
SetPower(2);
SetPower(30);
SetPower(3);
SetPower(4);

function SetPower($finder_id){

    $month_time = 30;//距离一个月
    $week_time = 7;//一个礼拜
    $date_now = date('Y-m-d H:i:s');

    $time_value = 0;//时间衰减数

    echo "开始计算设置power,现在时间是".$date_now."<br>finder_id-----------------------:".$finder_id;
    echo "<br>";

    $q_seed_power = mysql_query("select * from finder_seed where finder_id = '$finder_id'");

    $power_all = 0;
    $power_count = 0;
    $power_max = 0;

    while ($row_seed_power = mysql_fetch_array($q_seed_power))
    {
        $seed_type_bookmark_count = $row_seed_power["seed_type_bookmark_count"];//书签次数
        $seed_type_like_count = $row_seed_power["seed_type_like_count"];//喜欢次数

        $seed_last_update_time = $row_seed_power["time"];//时间

        $count = $row_seed_power["count"];//总次数
        $tags_id = $row_seed_power["tags_id"];//对应标签

        $is_tag = $row_seed_power["is_tag"];//是否订阅了

        $book_mark_score = $seed_type_bookmark_count;
        $like_score = $seed_type_like_count * 2;


        $zero1=strtotime ($date_now); //当前时间
        $zero2=strtotime ($seed_last_update_time);  //过年时间
        $differ_time=ceil(($zero1-$zero2)/86400); //60s*60min*24h

        if($differ_time >= $month_time){
            //相差超过一个月
            echo "相差超过一个月<strong>$differ_time</strong>天";
            $time_value = 0.3;

        }else if($differ_time >= $week_time && $differ_time < $month_time){
            //相差超过一个礼拜但是没有达到一个月
            echo "相差超过一个礼拜但是没有达到一个月<strong>$differ_time</strong>天";

            $time_value = 0.5;

        }else{
            //相差在一个礼拜之内
            echo "相差在一个礼拜之内<strong>$differ_time</strong>天";

            $time_value = 0.8;
        }

        $power = ($book_mark_score + $like_score) * $time_value;
        $power_all += $power;

        if($power >= $power_max){
            $power_max = $power;
        }

        echo "<br>";
        echo "power:".$power;
        echo "power_all:".$power_all;
        echo "<br>";

        echo "开始设置tags_id为".$tags_id."的power为$power<br>该power最后用户更新时间为".$seed_last_update_time;
        echo "<br><br>";
        mysql_query("UPDATE finder_seed SET power = '$power' where tags_id = '$tags_id' AND finder_id = '$finder_id'");

        $power_count ++;
    }

    echo "<br>";
    echo "最后power_all:".$power_all."；条数：".$power_count."$power_max:".$power_max;
    echo "<br>";
    powerDetail($finder_id, $power_all, $power_count, $power_max);
}

/**
 * @param $finder_id
 * @param $power_all ：power总数
 * @param $power_count : power数据量
 * @param $power_max : 最大的power
 */

function powerDetail($finder_id, $power_all, $power_count, $power_max){

    $q_seed_power_detail = mysql_query("select * from finder_seed where finder_id = '$finder_id'");

    $power_n = $power_max / 10; //转为10的倍数

    while ($row_seed_power = mysql_fetch_array($q_seed_power_detail))
    {
        $power = $row_seed_power["power"];
        $tags_id = $row_seed_power["tags_id"];//对应标签

        $power = $power / $power_n;
        $power = (int)$power;

        echo "最后得到的power：".$power;
        echo "<br><br>";
        mysql_query("UPDATE finder_seed SET power = '$power' where tags_id = '$tags_id' AND finder_id = '$finder_id'");
    }
}
?>