<?php

$username = $_POST['username'];
$password = $_POST['password'];
$gender = $_POST['gender'];
$experience = $_POST['experience'];
$tagString = $_POST['tags'];

if(strlen($username) <= 3){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// mysql inserting a new row
$result = mysql_query("INSERT INTO User(username, password, gender, experience) VALUES ('$username', '$password', '$gender', '$experience');");

// check if row inserted or not
if ($result) {
	
	// get user id
	$request = mysql_query("SELECT id from User where username='$username';");
	$row = mysql_fetch_array($request);
	$user_id = $row["id"];
	
	// split tags
	$tags = array();
	$tags = explode(',',$tagString);
	
	foreach($tags as $tag){	
	
		$request = mysql_query("SELECT id from Tag where name like '%$tag%';");
		$row = array();
		$row = mysql_fetch_array($request);
		
		if(empty($row)){
			
			$result = mysql_query("INSERT INTO Tag(name) VALUES ('$tag');");
			$request = mysql_query("SELECT id from Tag where name='$tag';");
			$row = mysql_fetch_array($request);
			$tag_id = $row["id"];
			
			$result = mysql_query("INSERT INTO Favorite_Tag(id_user, id_tag) VALUES ('$user_id', '$tag_id');");
		}
		else{
			$tag_id = $row["id"];
			$result = mysql_query("INSERT INTO Favorite_Tag(id_user, id_tag) VALUES ('$user_id', '$tag_id');");
		}

	}
	echo "OK";
} else 
{
	echo "There was a problem with registration. Try again later.";
} 
?>