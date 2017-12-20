<?php

$user_id = $_POST['user_id'];
$password = $_POST['password'];
$tagString = $_POST['tags'];

if(empty($user_id) || empty($password) || empty($tagString)){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$result = mysql_query("SELECT * FROM User WHERE id = '$user_id' and password = '$password';");
if(mysql_num_rows($result) == 1)
{
	//delete all tags
	$request = mysql_query("DELETE FROM Favorite_Tag WHERE id_user='$user_id';");

	// split tags
	$tags = array();
	$tags = explode(',',$tagString);

	foreach($tags as $tag)
	{	
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
}
 else
{
	echo "FAIL";
}
?>