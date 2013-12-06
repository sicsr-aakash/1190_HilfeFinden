<?php
$host="localhost";
$username="root";
$password="r00t123";
$db_name="android_api";
$db=mysql_connect($host, $username, $password) or die('Could not connect');
mysql_select_db($db_name, $db) or die('');

$result = mysql_query("SELECT distinct name FROM user_interest order by name");

$json = array();
$total_records = mysql_num_rows($result);

if($total_records >= 1){
  while ($row = mysql_fetch_array($result, MYSQL_ASSOC)){
    $json[] = $row;
  }
}

echo json_encode($json);
mysql_close($db);
