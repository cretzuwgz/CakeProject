<?php 

// array for JSON response
$response = array();

// check for required fields
 
$userId = $_POST['userId'];
$username = $_POST['username'];
$password = $_POST['password'];
$gender = $_POST['gender'];
$experience = $_POST['experience'];

if(empty($userId) || empty($username) || empty($password) || empty($gender) || empty($experience))
{
	echo "FAIL1";
	return 1;
}
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT * FROM User WHERE id = '$userId' and password = '$password';");

if(mysql_num_rows($result) == 1)
{
	$result2 = mysql_query("UPDATE User set username = '$username', gender ='$gender', experience = '$experience' where id = '$userId'");
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