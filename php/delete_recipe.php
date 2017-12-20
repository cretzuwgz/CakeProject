<?php

$recipe_id = $_POST['recipe_id'];

if(empty($recipe_id)){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$request = mysql_query("DELETE FROM Has_Favorite WHERE id_recipe='$recipe_id';");
$request = mysql_query("DELETE FROM Has_Tag WHERE id_recipe='$recipe_id';");
$request = mysql_query("DELETE FROM Req_Ingredient WHERE id_recipe='$recipe_id';");
$request = mysql_query("DELETE FROM Recipe WHERE id='$recipe_id';");


?>