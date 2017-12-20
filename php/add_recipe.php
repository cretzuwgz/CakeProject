<?php

$title = $_POST['title'];
$uploader = $_POST['uploader'];
$description = $_POST['description'];
$p_link = $_POST['p_link'];
$difficulty = $_POST['difficulty'];
$req_time = $_POST['req_time'];
$ingredString = $_POST['ingredients'];
$tagString = $_POST['tags'];

if(strlen($title) <= 3){
	echo "FAIL";
	return 1;
}

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// mysql inserting a new row
$result = mysql_query("INSERT INTO Recipe(title, uploader, description, p_link, difficulty, req_time) VALUES ('$title', '$uploader', '$description', '$p_link', '$difficulty', '$req_time');");

// check if row inserted or not
if ($result) {
	
	// get recipe id
	$request = mysql_query("SELECT id from Recipe where title='$title';");
	$row = mysql_fetch_array($request);
	$recipe_id = $row["id"];
	
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
	
	// split ingredients
	$ingredients = array();
	$ingredients = explode(',',$ingredString);
	
	foreach($ingredients as $ingredient){
		
		$split = array();
		$split = explode(':',$ingredient);
		$name = $split[0];
		$quantity = $split[1];
		$unit = $split[2];
	
		$request = mysql_query("SELECT id from Ingredient where name like '%$name%' and measurement like '%$unit%';");
		$row = array();
		$row = mysql_fetch_array($request);
		
		if(empty($row)){
			
			$result = mysql_query("INSERT INTO Ingredient(name,measurement) VALUES ('$name','$unit');");
			$request = mysql_query("SELECT id from Ingredient where name like '%$name%' and measurement like '%$unit%';");
			$row = mysql_fetch_array($request);
			$ingredient_id = $row["id"];
			
			$result = mysql_query("INSERT INTO Req_Ingredient(quantity, id_recipe, id_ingredient) VALUES ('$quantity', '$recipe_id', '$ingredient_id');");
		}
		else{
			$ingredient_id = $row["id"];
			$result = mysql_query("INSERT INTO Req_Ingredient(quantity, id_recipe, id_ingredient) VALUES ('$quantity', '$recipe_id', '$ingredient_id');");
		}

	}
	
	
	echo "OK";
} else {
	
	echo "There was a problem with registration. Try again later.";
} 
?>