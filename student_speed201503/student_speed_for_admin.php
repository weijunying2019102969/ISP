<?php
require_once('sys_conf.inc.php');
session_start();
if (isset($_SESSION['admin'])) {
// echo 'student_speed!!!!!<br>';
if (isset ( $_POST ["username"] ) && isset ( $_POST ["time"] ) && isset ( $_POST ["start_time"] ) && isset ( $_POST ["start_time"] )) {
	require_once ('sys_conf.inc.php');
// 	echo "-----------------------------------<br>";
	//获得测速者的IP
	function getIP() {
		if (getenv ( "HTTP_CLIENT_IP" ))
			$ip = getenv ( "HTTP_CLIENT_IP" );
		else if (getenv ( "HTTP_X_FORWARDED_FOR" ))
			$ip = getenv ( "HTTP_X_FORWARDED_FOR" );
		else if (getenv ( "REMOTE_ADDR" ))
			$ip = getenv ( "REMOTE_ADDR" );
		else
			$ip = "Unknow";
		return $ip;
	}

	//add by qijl 2015-03-22
	//是否是校外测速点
	function isOutSchool($username,$DBHOST, $DBUSER, $DBPWD, $DBNAME){
// 		echo "******************username = ".$username.'<br>';
		$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
		mysql_select_db ( $DBNAME );
		mysql_query ( "set names UTF8" );
		$sql_url_recent = "select * from sensor where sensor='" . $username."'";
		$result_url_recent = mysql_query ( $sql_url_recent );
		while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
			if($row_url_recent ["isOutSchool"]==1)
			{
// 				echo $username.' = true';
				return 1;
			}
			else {
// 				echo $username.' false';
				return 0;
			}
		}
	}
	//是否参与统计
	function isStatistics($username,$DBHOST, $DBUSER, $DBPWD, $DBNAME)
	{
	$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
		mysql_select_db ( $DBNAME );
		mysql_query ( "set names UTF8" );
		$sql_url_recent = "select * from sensor where id_sensor=" . $username;
		$result_url_recent = mysql_query ( $sql_url_recent );
		while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
			if($row_url_recent ["isStatistics"]==1)
			{
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	//是否参与显示
	function isShow($username,$DBHOST, $DBUSER, $DBPWD, $DBNAME)
	{
		$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
		mysql_select_db ( $DBNAME );
		mysql_query ( "set names UTF8" );
		$sql_url_recent = "select * from sensor where id_sensor=" . $username;
		$result_url_recent = mysql_query ( $sql_url_recent );
		while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
			if($row_url_recent ["isShow"]==1)
			{
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	//传感器是否参与统计
	function isStatisticsBySensor($sensor,$DBHOST, $DBUSER, $DBPWD, $DBNAME)
	{
		$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
		mysql_select_db ( $DBNAME );
		mysql_query ( "set names UTF8" );
		$sql_url_recent = "select * from sensor where sensor='" . $sensor."'";
		$result_url_recent = mysql_query ( $sql_url_recent );
		while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
			if($row_url_recent ["isStatistics"]==1)
			{
				return 1;
			}
			else {
				return 0;
			}
		}
	}


	//add end


	//add by qijl 2015-03-22
	//URL是否参与统计
	function isStatisticsUrl($url_id,$DBHOST, $DBUSER, $DBPWD, $DBNAME)
	{
		$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
		mysql_select_db ( $DBNAME );
		mysql_query ( "set names UTF8" );
		$sql_url_recent = "select * from url where id_url=" . $url_id;
		$result_url_recent = mysql_query ( $sql_url_recent );
		while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
			if($row_url_recent ["isStatistics"]==1)
			{
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	//add end


	//add by qijl 2015-03-22
	//学生速度是否正常
	function goZhiXinSuanfa($time, $url_id, $url_short, $step_num, $DBHOST, $DBUSER, $DBPWD, $DBNAME, $speedlog_table) {
		if ($time<6) {
			return 1;
		}
		else{
			$time_list = array ();
			$count_list = array ();
			for($i = 0; $i <= $step_num; $i ++) {
				$count_list [$i] = 0;
			}
	
			$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
			mysql_select_db ( $DBNAME );
			mysql_query ( "set names UTF8" );
			$sql_url_recent = "select * from " . $speedlog_table . " where url=" . $url_id;
			$result_url_recent = mysql_query ( $sql_url_recent );
			while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
				if ($row_url_recent ["time"] < 0 || $row_url_recent ["time"] > 30) {
					continue;
				} else {
					// echo $row_url_recent ["time"];
					// 				echo $row_url_recent["username"]." ";
					if(isStatistics($row_url_recent["username"],$DBHOST, $DBUSER, $DBPWD, $DBNAME))
					{
						array_push ( $time_list, $row_url_recent ["time"] );
					}
					// echo 'time_list[' . $i . '] = ' . $row_url_recent ["time"] . '<br>';
				}
			}
			// 		echo  count ( $time_list )."";
			$sum = 0;
			for($i = 0; $i < count ( $time_list ); $i ++) {
				$sum = $sum + $time_list [$i];
			}
			if (count ( $time_list )==0) {
				return 1;
			}
			else{
				$mu = $sum / count ( $time_list );
					
				// 求方差
				$sigma = 0;
				$sum0 = 0;
				for($i = 0; $i < count ( $time_list ); $i ++) {
					$sum0 = $sum0 + ($time_list [$i] - $mu) * ($time_list [$i] - $mu);
				}
				$sigma = sqrt ( $sum0 / count ( $time_list ) );
				// 置信区间上下界
				$lower = $mu - $sigma * 1.96 / sqrt ( count ( $time_list ) );
				$heigher = $mu + $sigma * 1.96 / sqrt ( count ( $time_list ) );
					
				// echo 'mu = '.$mu.'<br>';
				// echo 'sigma = '.$sigma.'<br>';
				// echo 'lower = '.$lower.'<br>';
				// echo 'heigher = '.$heigher.'<br>';
					
				// 最大值，最小值
				$time_min = min ( $time_list );
				$time_max = max ( $time_list );
					
				// 将数据分成1000
				if (($time_max - $time_min) != 0) {
					// $step=($time_max-$time_min)/$step_num;
						
					// //每一段的概率密度
					// $i=0;
					// while ($i<count($time_list)) {
						
					// $temp=($time_list[$i]-$time_min)/$step;
					// $index=floor($temp);//向下取整
					// $count_list[$index]++;
					// $i++;
					// }
					// $time=1.065;
					if ($time > $heigher) {
						return 0;//不正常
						// 				echo "您访问" . $url_short . "的速度正常";
					} else {
						// echo $time;
						return 1;//速度正常
						// 				echo "您访问" . $url_short . "的速度不正常，请联系东北师范大学信息化管理与规划办公室。联系电话：85099005";
					}
				} else {
					// 			echo "您访问的" . $url_short . "速度正常";
					return 1;
				}
			}

		}


	}

	//add end
	//获得学生IP的网络地址
	function and_func($ip, $mark) {
		$ip_arr = explode ( ".", $ip );
		$mark_arr = explode ( ".", $mark );
		$and = "";
		for($i = 0; $i < 4; $i ++) {
			if ($i == 0) {
				$and = ( int ) $ip_arr [$i] & ( int ) $mark_arr [$i];
			} else {
				$and = $and . '.' . (( int ) $ip_arr [$i] & ( int ) $mark_arr [$i]);
			}
		}
		return $and;
	}

	$username = $_POST ["username"];
	$time = $_POST ["time"];
	$start_time = $_POST ["start_time"];
	$browser = $_SERVER ['HTTP_USER_AGENT'];
	$ip = getIP ();
	// $ip = "222.27.105.250";
	$url = $_POST ["url"];
	$now = date ( 'Y-m-d H:i:s' );
// 	echo 'url:' . $url . ' username = ' . $username . ' time = ' . $time . ' start_time = ' . date ( 'Y-m-d H:i:s', $start_time ) . ' ip = ' . $ip;
	$urllist = array ();
	
	$link_id = mysql_connect ( $DBHOST, $DBUSER, $DBPWD );
	mysql_select_db ( $DBNAME );
	mysql_query ( "set names UTF8" );
	// 将用户信息直接存入数据库student_speed中
	$str = "INSERT INTO " . $student_speed . " (student_url,student_speed,student_ip,time)
        VALUES ('" . $url . "','" . $time . "','" . $ip . "','" . $now . "')";
	$result = mysql_query ( $str, $link_id );
	// echo 'result = '.$result.'<br>';

	$sensor_name = "未知地点";	//您所在楼宇的名称
	$sensor_ip = "";			//您所在楼宇的测速点的IP
	$sensor_speed = "未知速度";	//您所在楼宇的测速点访问URL的速度
	$sensor_id = "";	
	$sensor_mark = "";
	$week_avg = "未知速度";		//您所在楼宇一周访问URL的速度

	// 读取测速点IP
	$file = 'ip_mark.txt';
	$content = file_get_contents ( $file );
	// echo $content.'<br><br><br><br>-------------------------<br>';
	$array_content = array ();
	$array_content = explode ( "\n", $content ); //
	// echo 'array_content.size = '.count($array_content).'<br>';
	$rel_list = array ();
	for($i = 0; $i < count ( $array_content ); $i ++) {
		// echo $array_content[$i].'<br>';
		$temp = array ();
		$temp = preg_split ( '/[\s,;]+/', $array_content [$i] );
		$localhost_arr = and_func ( $ip, $temp [2] );
		// echo 'localhost_arr='.$localhost_arr.'<br>';
		$ip_arr = and_func ( $temp [1], $temp [2] );
		// echo 'ip_arr'.$ip_arr.'<br>';
		if ($localhost_arr == $ip_arr) {
			// echo 'in '.$temp[0].'-----------<br>';
			$sensor_name = $temp [0];
			$sensor_ip = $temp [1];
			$sensor_mark = $temp [2];
			// echo 'sensor_ip= '.$sensor_ip.'<br>';
			break;
		} else { // echo 'else<br>';
			continue;
		}
	}
	// echo $ip_add.'<br>';

	$sql_url = "select * from url";
	$result_url = mysql_query ( $sql_url );
	$url_short = "未知网址";
	while ( $row_url = mysql_fetch_array ( $result_url ) ) {
		if ($url == $row_url ["url"]) {
			$url_id = $row_url ["id_url"];
			$url_short = $row_url ["short_url"];
			break;
		}
	}

	$sensor_recent = array ();
	$sensorlist = array ();
	$timelist = array ();
	$jilin_speed = 0;
	
	
	
	
	$now=date('Y-m-d H:i:s',time());
// 	$outtime=strtotime($now)-strtotime($start_time);	
	
	$count_forAvg=0;
	$time_forAvg=0;
	$sql_url_recent = "select * from " . $speedlog_table . " where url=" . $url_id . " order by starttime desc ";
	$result_url_recent = mysql_query ( $sql_url_recent );

// 	//modify by qijl 2015-03-22
	
	while ( $row_url_recent = mysql_fetch_array ( $result_url_recent ) ) {
		if ((strtotime($now)-strtotime($row_url_recent["starttime"]))<=$current_timeout) {
			// 拿到所有测速点当前的速度
			if (! in_array ( $row_url_recent ["username"], $sensor_recent )) {
				array_push($sensor_recent, $row_url_recent ["username"]);
				// 搜索测速点的名字
				if(isShow($row_url_recent ["username"], $DBHOST, $DBUSER, $DBPWD, $DBNAME))
				{
					$sql_sensor_search = "select * from sensor where id_sensor='" . $row_url_recent ["username"] . "'";
					$result_sensor_search = mysql_query ( $sql_sensor_search );
					while ( $row_sensor_search = mysql_fetch_array ( $result_sensor_search ) ) {
						if(isStatistics($row_url_recent ["username"], $DBHOST, $DBUSER, $DBPWD, $DBNAME))
						{
							$count_forAvg++;
							$time_forAvg = $time_forAvg + $row_url_recent ["time"];
						}
	// 					echo "sensor = ".$row_sensor_search ["sensor"];
						array_push ( $sensorlist, $row_sensor_search ["sensor"] );
						if ($row_url_recent ["time"] == - 1)
						{
	// 						echo " time = ".$row_url_recent ["time"].'<br>';
							array_push ( $timelist, 30 );
						}
	
						else{
	// 						echo " time = ".$row_url_recent ["time"].'<br>';
							// 						echo $row_url_recent ["time"].'**';
							array_push ( $timelist, $row_url_recent ["time"] );
						}
	
	
						// 如果当前测速点的IP和我楼宇的测速点的IP是一致的，就得到了当前我所在楼宇的IP
					if ($row_sensor_search ["sensor"] == $sensor_name) {
							$sensor_speed = $row_url_recent ["time"];
							$sensor_id = $row_url_recent ["username"];
						}
	// 					break;
					}
				}
	// 			break;
			}
				
		}
	}
	// 	echo $count_forAvg;
	// 	echo $time_forAvg;
	if($count_forAvg==0){$time_forAvg=0;}
	else {$time_forAvg = $time_forAvg / $count_forAvg;}
	// 	echo $time_forAvg;
	//modify end

	if ($sensor_id != null) {
		// 您所在的楼宇测速点一周内访问url的平均速度
		$week = 7 * 24 * 60 * 60;
		$begin_time = strtotime ( date ( 'Y-m-d H:i:s', time () ) );
		$week_avg = 0;
		$sum = 0;
		$count = 0;
		// echo $url_id.' '.$sensor_id.'<br>';
		$sql_your_sensor_week_avg_speed = "select * from " . $speedlog_table . " where url=" . $url_id . " and username=" . $sensor_id . " order by starttime desc";
		$result_your_sensor_week_avg_speed = mysql_query ( $sql_your_sensor_week_avg_speed );
		while ( $row_your_sensor_week_avg_speed = mysql_fetch_array ( $result_your_sensor_week_avg_speed ) ) {
			// 			echo $row_your_sensor_week_avg_speed["time"]."#################";
			if (($begin_time - strtotime ( $row_your_sensor_week_avg_speed ["starttime"] )) < $week) {
				// 				if(isStatisticsUrl($url_id,$DBHOST, $DBUSER, $DBPWD, $DBNAME))
					// 				{
				$sum = $sum + $row_your_sensor_week_avg_speed ["time"];
				$count ++;
				// 				}
			} else
				break;
		}
		if ($count == 0) {
			$week_avg = 0;
		} else {
			$week_avg = $sum / $count;
			// 			echo $sum."****".$count."***";
			// 			echo $week_avg;
		}
	}
	// 	mysql_close ( $link_id );

	if (isset ( $_POST ['time'] )) {
		if ($time == -1) {
			array_push ( $sensorlist, "您的速度" );
			array_push ( $timelist, "30" );
		}
		else 
		{
			array_push ( $sensorlist, "您的速度" );
			array_push ( $timelist, $time );
		}
		
	}

	if (count ( $sensorlist ) == count ( $timelist )) {
		$sensor_recent = array_combine ( $sensorlist, $timelist );
		asort ( $sensor_recent );
// 		print_r($sensor_recent);
		?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--[if IE7]><script language="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<link rel="stylesheet" type="text/css" href="js/jquery.jqplot.css" />
<script language="javascript" type="text/javascript"
	src="js/jquery.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/excanvas.js"></script>
<script language="javascript" type="text/javascript"
	src="js/jquery.jqplot.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.cursor.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.highlighter.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.dateAxisRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
	src="js/plugins/jqplot.barRenderer.min.js"></script>



<link rel="stylesheet" type="text/css" href="bootstrap-combined.min.css">

<link rel="stylesheet" type="text/css" href="index.css">

<style type="text/css">
input.select{ vertical-align:middle};
font{ size:14pt};
</style>


<title>student_speed</title>
</head>
<body>




<?php
// require_once('sys_conf.inc.php');

$urlkeylist=array();
$urlidlist=array();
// $link_id=mysql_connect($DBHOST,$DBUSER,$DBPWD);
// mysql_select_db($DBNAME);
// mysql_query("set names UTF8");

// modify by qijl 2015-03-22
$sql_url_t="select * from url where isShow='1'";
$result_url_t=mysql_query($sql_url_t);
while ($row_url_t=mysql_fetch_array($result_url_t)){
        array_push($urlkeylist, $row_url_t["short_url"]);
        array_push($urlidlist, $row_url_t["id_url"]);
}
mysql_close($link_id);
// echo 'urllist size = '.count($urlidlist).'<br>';
// var_dump($urllist);
// var_dump($urlnamelist);
// $url_combine = array_combine($urllist,$urlnamelist);
// var_dump($url_combine);
//modify end
?>


<div style="background-color:#f2f2f2;height:100px; vertical-align:middle">
<table width="60%" align="center" height="100"><tr><td width="330px" align="right" valign="middle"> 
  <a href="../student_speed201503/index.php"><img src="../student_speed201503/logof2.jpg" width="304" height="76" border="0"></a><img src="v1.gif" width="25" height="43"></td>
<td width="150px" align="right" style="padding-top:18px ">
<select name="url_name" id="select_url_name">
<?php
echo '<option style="font-size:14px;max-width:200px;" value="select">选择测速的目标网站</option>';
for ($i=0;$i<count($urlidlist);$i++)
{
        echo '<option style="font-size:14px;height:30px;max-width:200px;" value='.$urlidlist[$i].'>'.$urlkeylist[$i].'</option>';
}
?>
</select></td><td align="left" width="60px" style="padding-top:8px;" >
			<input value="我要测速" class="btn btn-fl" type="submit" onclick="geturl()" >
         </td></tr></table>
<script type="text/javascript">
function geturl()
{
        var url=document.getElementById("select_url_name").value;
        if(url=="select")
    	{
    		alert("选择测速的目标网站");
    	}
    	else if(url!=""){
                var nextpage = "student_speed_frame.php?select_url="+url;
                window.location.href=nextpage;
                document.form.submit();
        }
        else{
                alert("请选择要测速的网址");
        }
}
</script>

</div>
        </div>


        </div>
</div>

<br>
	<center>

		<script type="text/javascript">
		$(document).ready(function(){
			var sensor_time = [];
			var avg_time=[];
			<?php
		$sum_time = 0;
		foreach ( $sensor_recent as $sensor_s => $time_t ) {
			echo 'sensor_time.push(["' . $sensor_s . '", "' . sprintf("%.2f",$time_t) . '","' . $sensor_s . '", "' . sprintf("%.2f",$time_t) . '"]);';
// 			echo 'sensor_time.push(["000", "' . sprintf("%.2f",$time_t) . '"]);';
			if ($sensor_s == "吉林大学")
			{
				if($time_t==-1)	$jilin_speed = "30";
				else $jilin_speed = $time_t;
			}
				
			//$sum_time = $sum_time + $time_t;
		}
		//$avg_time = $sum_time / count ( $sensor_recent );
		$avg_time = $time_forAvg;
// 		 echo 'avg_time = '.$avg_time.'<br>';
		$sensor_recent_keys = array_keys ( $sensor_recent );
		// echo '$sensor_recent_keys[0] = '.$sensor_recent_keys[0];
		// echo '$sensor_recent_keys[count($sensor_recent)-1] = '.$sensor_recent_keys[count($sensor_recent)-1];
		echo 'avg_time.push(["' . $sensor_recent_keys [0] . '", "' . sprintf("%.2f",$avg_time) . '", "平均速度", "' . sprintf("%.2f",$avg_time) . '"]);';
		echo 'avg_time.push(["' . $sensor_recent_keys [count ( $sensor_recent ) - 1] . '", "' . sprintf("%.2f",$avg_time) . '", "平均速度", "' . sprintf("%.2f",$avg_time) . '"]);';
		?>

			//modify by qijl 2015-03-13
			var plot1 = $.jqplot('chart1', [sensor_time, avg_time,<?php echo $time?>,<?php echo $time?>], {
				//modify end
				
				//modify by qijl 2015-03-12
		    title: '<strong>速度对比图</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size="1">目标网站：<?php echo $url_short;?></font>',
			  	//modify end 

		    //add by qijl 2015-03-13
// 		    #d8b83f
		    seriesColors: [ "#00AEBE", "#00FF00", "#FF0000","#FF1493"],
		  	//add end 
		  	
			seriesDefaults: {
				showMarker:false,
				rendererOptions: {
					barWidth: 20,
					varyBarColor : true
				},
			},
		    series:[{renderer:$.jqplot.BarRenderer, label: '校内固定测速点速度',seriesColors: [
			    <?php
		for($i = 0; $i < count ( $sensor_recent_keys ); $i ++) {
			if ($sensor_recent_keys [$i] == "您的速度") {
				if ($i == count ( $sensor_recent_keys ) - 1)
					echo '"#FF0000"';
				else
					echo '"#FF0000",';
			} else {
				if ($i == count ( $sensor_recent_keys ) - 1)
				{
// 					if (isStatisticsBySensor($sensor_recent_keys[$i], $DBHOST, $DBUSER, $DBPWD, $DBNAME)==1)
// 						echo '"#00AEBE"';
					if (isOutSchool($sensor_recent_keys[$i], $DBHOST, $DBUSER, $DBPWD, $DBNAME))
						echo '"#FF1493"';
					else 
						echo '"#00AEBE"';
				}
				else
				{
// 					if (isStatisticsBySensor($sensor_recent_keys[$i], $DBHOST, $DBUSER, $DBPWD, $DBNAME)==1)
// 						echo '"#00AEBE",';
					if (isOutSchool($sensor_recent_keys[$i], $DBHOST, $DBUSER, $DBPWD, $DBNAME))
						echo '"#FF1493",';
					else 
						echo '"#00AEBE",';
				}
			}
		}
		?>
					//modify by qijl 2015-03-13
				    ]}, {xaxis:'xaxis', yaxis:'yaxis',label: '平均速度'},{xaxis:'xaxis', yaxis:'yaxis',label: '您的速度'},{xaxis:'xaxis', yaxis:'yaxis',label: '校外固定测速点速度'}],
					//modify end
					
			axesDefaults: {
		        tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
		        tickOptions: {
		          angle: -30,
		          fontSize: '10pt'
 		        }
		    },
		    axes: {
		      xaxis: {
// 		    	label: "测速点",
 		        renderer: $.jqplot.CategoryAxisRenderer,
		        tickRenderer: $.jqplot.CanvasAxisTickRenderer
		      },
		      yaxis: {
				label: "耗时(秒)",
// 				autoscale:true,
		 		min: 0,      // 横（纵）坐标显示的最小值  
				max: <?php echo ($timeout/1000);?>,
				//add by qijl 2015-03-19
		        numberTicks: 7
		        //add end
				
		      }
		    },
		    highlighter: {
				show: true,
		 		sizeAdjust: 7,
			    showMarker:false,
			    showTooltip: true,
			    tooltipAxes: 'xy',
			    yvalues: 3,  // !!!这行很重要！
			    formatString:'<table class="jqplot-highlighter"> \
			      <input type="hidden" value="%s"><input type="hidden" value="%s"> \
			      <tr><td>测速点:</td><td>%s</td></tr> \
			      <tr><td>耗时:</td><td>%s</td></tr> \
			      </table>'
			},
		    legend: {
				show: true,//设置是否出现分类名称框(即所有分类的名称出现在图的某个位置)
				location: 'ne', //分类名称框出现位置, nw, n, ne, e, se, s, sw, w.
				xoffset: 12, //分类名称框距图表区域上边框的距离(单位px)
				yoffset: 12, //分类名称框距图表区域左边框的距离(单位px)
				background:'#FF0000', //分类名称框距图表区域背景色
				textColor:'#FF0000' //分类名称框距图表区域内字体颜色
			}
		  });
		});
		</script>

		<div id="chart1" style="position: relative; height: 60%; width: 80%;"></div>
		<br>

		<table width="70%" bgcolor="#ff0000">
			<tr>
				<td width="40%">
	<strong>您的测速报告</strong><br>
	<br>
	

             <?php
		 if(goZhiXinSuanfa ( $time, $url_id, $url_short, $step_num, $DBHOST, $DBUSER, $DBPWD, $DBNAME, $speedlog_table )==1)
		 {
		 	echo "您的速度<font color='#ff0000'>正常</font>";
		 }else{
			echo "您的速度<font color='#ff0000'>不正常</font>&nbsp;可拨打信息化办服务热线85099005申报故障排除。";
		}
		?><br>
		算法：该结论依据置信区间的数学模型。<br>
		数据一：您访问"&nbsp;<?php echo $url_short;?>&nbsp;"的速度为&nbsp;<font color="#ff0000"><?php
		if ($time == -1) {
			echo "30.00";
		}else{
			echo sprintf("%.2f",$time);
		}
		?></font>&nbsp;秒。<br>
		数据二：您所在楼宇固定测速点访问"&nbsp;<?php echo $url_short;?>&nbsp;"的速度为<font color="#ff0000">&nbsp;<?php
		if ($sensor_speed == -1) {
			echo "30.00";
		}else{
			echo sprintf("%.2f",$sensor_speed);
		}
		?></font>&nbsp;秒。<br>
		
               数据三：吉林大学访问 "&nbsp;<?php echo $url_short;?>&nbsp;"的速度为 <font color="#ff0000"><?php if ($jilin_speed==-1)echo "30.00"; elseif($jilin_speed==0)echo "_.__"; else echo sprintf("%.2f",$jilin_speed);?></font>&nbsp;秒。<br>
               数据四：全部固定测速点访问"&nbsp;<?php echo $url_short;?>&nbsp;"的平均速度为<font color="#ff0000">&nbsp;<?php if ($avg_time == -1) echo sprintf("%.2f","30"); else echo sprintf("%.2f",$avg_time);?></font>&nbsp;秒。<br>
				<!-- 		modify end -->


				</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	         <td><strong>延伸阅读</strong> <br>

                  <br>为了更加科学的理解您的网速，欢迎您花时间阅读以下内容。
		  <br>我们对网速问题负有有限责任，包括：
          <br>（1）利用可能的技术手段对学校有限的总带宽进行合理与充分利用；（2）室内墙壁网络接口以上链路的稳定与通畅；（3）上网认证系统的正常运行。
		  <br>除上述因素外，用户用网体验还受到其他信息化办所不能控制的诸多因素影响，包括
		  <br>（1）学校总带宽所决定的人均带宽限制（正常用网时段内随用户规模的变化，人均带宽在290K到1000K之间）；（2）从室内墙壁网络接口到计算机的网线质量；（3）用户自行配备的小型交换机(集线器)性能；（4）个人计算机性能、所用浏览器类型及其设置等因素。


		 </td></tr>	

             </table>
	</center>
<?php
	} else
		echo 'search wrong';
	?>
</body>
</html>
<?php
} else {
	header ( 'Location: index.php' );
}
}
else header("Location: ../speed201503/login.php");
?>
