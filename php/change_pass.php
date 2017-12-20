<?php 

// array for JSON response
$response = array();

// check for required fields
 
$userId = $_POST['userId'];
$oldPass = $_POST['oldPass'];
$newPass = $_POST['newPass'];

if(empty($userId) || empty($oldPass) || empty($newPass))
{
	echo "FAIL1";
	return 1;
}
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get a product from products table
$result = mysql_query("SELECT * FROM User WHERE id = '$userId' and password = '$oldPass';");

if(mysql_num_rows($result) == 1)
{
	$result2 = mysql_query("UPDATE User set password = '$newPass' where id = '$userId'");
	if ($result2) 
	{
		echo "OK";
	}
	else
	{
		echo "FAIL2";
	}
 }
 else
{
	echo "FAIL3";
}
?>