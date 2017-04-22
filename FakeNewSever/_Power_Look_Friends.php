<?php
/**
 * Created by PhpStorm.
 * User: wwnje
 * Date: 2017/4/10
 * Time: 下午3:35
 * 寻找好朋友
 */

require 'mysql_connect.php';
require '_Power.php';

/**
 * 开始寻找好友
 * 1.查看其他用户 有和finder最高power标签的用户数量
 *
 */
function lookFriends($finder_id, $data){

    $finder_like_tags_id = $data[0] -> tags_id;//最喜欢的标签id

    $q_seed_friend_count = mysql_query("select count(*) from finder_seed where finder_id != '$finder_id' && tags_id = $finder_like_tags_id");
    $r_friend_count = mysql_fetch_row($q_seed_friend_count);
    $r_friend_count = $r_friend_count[0];

    echo "共有$r_friend_count 条对 $finder_like_tags_id 有兴趣的用户数据";

    $data_friends = array(); //定义好一个数组.PHP中array相当于一个数据字典.

    $q_seed_power_friends = mysql_query("select * from finder_seed where finder_id = '$finder_id'");

    while ($row_seed_power_friends = mysql_fetch_array($q_seed_power_friends))
    {
        
    }

    //$data_count = count($data_friends);
}


//需要寻找进行推荐的finder
$finder_id = "30";

SetPower($finder_id);

$q_seed_count = mysql_query("select count(*) from finder_seed where finder_id = '$finder_id'");
$r_count = mysql_fetch_row($q_seed_count);
$r_count = $r_count[0];

if($r_count <= 3){
    echo "$finder_id 种子信息太少,无法进行推荐";
}else{
    echo "$finder_id 有 $r_count 条数量,开始进行推荐算法设计";
    getFinderTopPower($finder_id);
}

/**
 * finder获取power最高的几条
 * 1.初始化计算该用户所有power
 * 2.计算该用户的power数量
 * 3.数量大于3时找power最高的3条
 */


function getFinderTopPower($finder_id){
    $data = array(); //定义好一个数组.PHP中array相当于一个数据字典.

    $q_seed_power = mysql_query("select * from finder_seed where finder_id = '$finder_id'");

    while ($row_seed_power = mysql_fetch_array($q_seed_power))
    {

        $seed = new Seed_Messge();

        $seed -> power = $row_seed_power["power"];//书签次数
        $seed -> tags_id = $row_seed_power["tags_id"];//书签次数

        $data[] = $seed;
    }

    $data_count = count($data);
    echo $data_count;
    echo "<br>";

    $max = $data[0] -> power;
    $min = $max;

    //排序
    for($i = 0; $i < $data_count; $i ++){
        for($j = 0; $j < $data_count - 1 - $i; $j ++){

            if($data[$i] -> power > $data[$i + 1] -> power){
                $tmp_power = $data[$i] -> power;
                $tmp_id = $data[$i] -> tags_id;

                $data[$i] -> power = $data[$i + 1] -> power;
                $data[$i] -> tags_id = $data[$i + 1] -> tags_id;

                $data[$i + 1] -> power = $tmp_power;
                $data[$i + 1] -> tags_id = $tmp_id;
            }
        }
    }

    echo "排序完成<br>";

    for($i = 0; $i < $data_count; $i ++){
        echo $data[$i] -> tags_id.",".$data[$i] -> power;
        echo "<br>";
    }

    //查找好友
    lookFriends($finder_id, $data);
}


class Seed_Messge
{
    public $tags_id;//iPhone
    public $power;// 5分
}
?>