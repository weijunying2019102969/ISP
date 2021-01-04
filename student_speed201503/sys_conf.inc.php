<?php

$DBHOST="127.0.0.1";
$DBUSER="root";
$DBPWD="59568352";
$project="speed201503";

// $DBHOST="localhost";
// $DBUSER="root";//
// $DBPWD="111111";//
// // $project="speed201407";
// $project="speed201503";

// $DBNAME="outlier";
$DBNAME="speed201503";
$speedlog_table="speedlog";
$student_info="student_info";
$student_speed="student_speed";
$key="xilihutu";
$PAGE_MAX_LINE=5; 

$reload_time=15*60; // in sec for next test suite.
$timeout=30*1000; // in  millisecond for current web site.
$refresh_page='/'.$project.'/refresh.php';
$frame_page='/'.$project.'/frame.php';
$end_page='/'.$project.'/end.php';
$localhost='127.0.0.1';
$timeout_flag = -1.0; // time cost equal this value means 'time out' happened.
$step_num = 100; //outlier step number.
$confidence = 1.96;//

/**
modify by qijl 2015-03-11
 */
$current_timeout=0.5*60*60;//0.5 hours
/**
end
 */

$default_time=2*24*60*60;//time_speed sensor_speed url_speed chart default x

class record {
     public $url;
     public $key_url;
     public $time;
     public $start_time;
     public $username;
     public $browser;
     public $ip;
}

// $all=get_all_list($DBHOST, $DBUSER, $DBPWD, $DBNAME);
// print_r($all);


?>

<?php 
class url_sensor
{
	public $id_url;
	public $key_url;
	public $url;
	public $id_sensor;
	public $sensor;
}

function get_all_list($DBHOST, $DBUSER, $DBPWD, $DBNAME)
{
	$all=array();
	$link_id=mysql_connect($DBHOST,$DBUSER,$DBPWD);
	mysql_select_db($DBNAME);
	mysql_query("set names UTF8");
	
	$sql_final_log="select * from sensor_url_relation";
	$result_final_log=mysql_query($sql_final_log);
	while ($row_final_log=mysql_fetch_array($result_final_log)){
		$final_time=$row_final_log["time"];
	}
	if (isset($final_time)) {
		$sql_search_relation="select * from sensor_url_relation where selected=1 and time='".$final_time."'";
		$result_search_relation=mysql_query($sql_search_relation);
		while ($row_search_relation=mysql_fetch_array($result_search_relation))
		{
			$url_sensor=new url_sensor();
			$url_sensor->id_url=$row_search_relation["url"];
			$url_sensor->id_sensor=$row_search_relation["sensor"];
			$sql_search_url="select * from url where id_url=".$url_sensor->id_url;
			$result_search_url=mysql_query($sql_search_url);
			while ($row_search_url=mysql_fetch_array($result_search_url))
			{
				$url_sensor->key_url=$row_search_url["short_url"];
				$url_sensor->url=$row_search_url["url"];
	// 			echo $url->key_url.'  '.$url->url.'<br>';
			}
			$sql_search_sensor="select * from sensor where id_sensor=".$url_sensor->id_sensor;
			$result_search_sensor=mysql_query($sql_search_sensor);
			while ($row_search_sensor=mysql_fetch_array($result_search_sensor))
			{
				$url_sensor->sensor=$row_search_sensor["sensor"];
	// 			echo $sensor->sensor.'<br>';
			}
	// 		echo $url->id_url.'  '.$sensor->id_sensor.'<br>';
			array_push($all, $url_sensor);
		}
	}
	mysql_close($link_id);
	return $all;
}

?>