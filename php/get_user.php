<?php 
 
// array for JSON response
$response = array();

// check for required fields
 
$username = $_POST['username'];
$password = $_POST['password'];
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT id, username, date, gender, experience FROM User WHERE username = '$username' and password = '$password';");

$response["user"] = array(); 

if(mysql_num_rows($result) ==1)
{
	$row = mysql_fetch_array($result);
	$product = array();
	$userId = $row["id"];
	$product["id"] = $userId;
	$product["username"] = $row["username"];
	$product["date"] = $row["date"];
	$product["gender"] = $row["gender"];
	$product["experience"] = $row["experience"];
	$product["tags"] = array();
	
	$result2 = mysql_query("SELECT name FROM Favorite_Tag JOIN Tag on (Favorite_Tag.id_tag = Tag.id) where Favorite_Tag.id_user = '$userId'");
	while($row2 = mysql_fetch_array($result2))
	{
		array_push($product["tags"], $row2["name"]);
	}
	array_push($response["user"], $product);
 }
  
// echoing JSON response
echo json_encode($response);
?>