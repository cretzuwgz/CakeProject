<?php

$recipe_id = $_POST['recipe_id'];
$user_id = $_POST['user_id'];

if(empty($recipe_id) || empty($user_id)){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$request = mysql_query("INSERT into Has_Favorite(id_user,id_recipe) values('$user_id','$recipe_id');");

?>