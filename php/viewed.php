<?php

$recipe_id = $_POST['recipe_id'];

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$result = mysql_query("UPDATE Recipe SET accessed = accessed + 1 WHERE id='$recipe_id';");
	
?>