<?php

$recipe_id = $_POST['recipe_id'];
$title = $_POST['title'];
$reqTime = $_POST['req_time'];
$difficulty = $_POST['difficulty'];
$description = $_POST['description'];
$tagString = $_POST['tags'];

if(empty($recipe_id) || strpos($recipe_id,'or') !==false){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

//update recipe
$request = mysql_query("UPDATE Recipe SET title='$title',req_time='$reqTime',difficulty='$difficulty',description='$description' WHERE id='$recipe_id';");

//delete all tags

$request = mysql_query("DELETE FROM Has_Tag WHERE id_recipe='$recipe_id';");

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
		
		$result = mysql_query("INSERT INTO Has_Tag(id_recipe, id_tag) VALUES ('$recipe_id', '$tag_id');");
	}
	else{
		$tag_id = $row["id"];
		$result = mysql_query("INSERT INTO Has_Tag(id_recipe, id_tag) VALUES ('$recipe_id', '$tag_id');");
	}
}

echo "OK";
?>