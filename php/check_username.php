<?php 

$username = $_POST['username'];
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get a product from products table
$result = mysql_query("SELECT count(*) FROM User WHERE lower(username) = lower('$username');");
$row = mysql_fetch_row($result);

echo $row[0];
?>