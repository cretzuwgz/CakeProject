<?php 
/*
 * Following code will get recommanded recipes
 */
 
$user_id = $_POST['user_id'];
 
// array for JSON response
$response = array();
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

//get the favorites recipes ids
$result = mysql_query("SELECT id_recipe FROM Has_Favorite WHERE id_user = $user_id ;");
$fav_recipes = '';
$fav_recipes_array = array();

if($result){
	while($row = mysql_fetch_array($result)){
		$fav_recipes_array[] = $row["id_recipe"];
	}
}
$fav_recipes = implode(",",$fav_recipes_array);

//get the uploaded recipes ids
$result = mysql_query("SELECT id FROM Recipe WHERE uploader = $user_id ;");
$uploaded_recipes = '';
$uploaded_recipes_array = array();

if($result){
	while($row = mysql_fetch_array($result)){
		$uploaded_recipes_array[] = $row["id"];
	}
}
$uploaded_recipes = implode(",",$uploaded_recipes_array);

//get all the recipes excluding Favoriles and Uploaded as an array
$all_recipes_array = array();
$excluded = $fav_recipes.",".$uploaded_recipes;
$result = mysql_query("SELECT id FROM Recipe WHERE id not in ($excluded);");
if($result){
	while($row = mysql_fetch_array($result)){
		$all_recipes_array[] = $row["id"];
	}
}

$recommanded_recipes_array = array();

foreach($fav_recipes_array as $fav_recipe){
	
	//get tags for this fav_recipe
	$result = mysql_query("SELECT id_tag FROM Has_Tag WHERE id_recipe = $fav_recipe;");
	$tags = '';

	if($result){
		while($row = mysql_fetch_array($result)){
			if(strlen($tags)==0)
				$tags = $row["id_tag"];
			else
				$tags = $tags.",".$row["id_tag"];
		}
	}

	foreach($all_recipes_array as $recipe){
		$result = mysql_query("SELECT count(*) from Has_Tag WHERE id_recipe=$recipe and id_tag in ($tags)");
		if($result){
			$row = mysql_fetch_array($result);
			$no_tags = (int) $row["count(*)"];
			if($no_tags >= 2){
				$recommanded_recipes_array[] = $recipe;
			}
		}	
	}
}

//get recipes with common fav tags
$result = mysql_query("SELECT id_recipe FROM Has_Tag INNER JOIN Favorite_Tag USING (id_tag) WHERE id_user = $user_id ;");
if($result){
	while($row = mysql_fetch_array($result)){
		$recommanded_recipes_array[] = $row["id_recipe"];
	}
}
$recommanded_recipes = implode(",",$recommanded_recipes_array);

// get the recommanded recipes
$result = mysql_query("SELECT * FROM Recipe WHERE id in ($recommanded_recipes) order by top_param DESC;");

$response["recipes"] = array(); 
if($result){
	while($row = mysql_fetch_array($result))
	{
		$product = array();
		$product["id"] = $row["id"];
		$product["title"] = $row["title"];
		$product["date"] = $row["date"];
		
		$uploaderID = $row["uploader"];
		$up_response = mysql_query("SELECT username FROM User where id =$uploaderID;");
		$uploader = mysql_fetch_array($up_response);
		$product["uploader"] = $uploader["username"];
		
		$product["description"] = $row["description"];
		$product["p_link"] = $row["p_link"];
		$rating = $row["rating"];
		$rating = floatval($rating);
		$rating = round($rating);
		$product["rating"] = $rating;
		$product["difficulty"] = $row["difficulty"];
		$product["req_time"] = $row["req_time"];
		$product["tags"] = array();
		
		$id = $product["id"];
		$tag_result = mysql_query("SELECT * FROM Has_Tag join Tag on(id_tag = id) where id_recipe = $id;");
		if($tag_result){
			while($tag_row = mysql_fetch_array($tag_result)){
				array_push($product["tags"], $tag_row["name"]);
			}
		}
		
		$product["ingredients"] = array();
		$tag_result = mysql_query("SELECT * FROM Req_Ingredient join Ingredient on(id_ingredient = id) where id_recipe = $id;");
		if($tag_result){
			while($tag_row = mysql_fetch_array($tag_result)){
				$ingrd =  array();
				$ingrd["name"] = $tag_row["name"];
				$ingrd["quantity"] = $tag_row["quantity"];
				$ingrd["unit"] = $tag_row["measurement"];
				array_push($product["ingredients"], $ingrd);
			}
		}
	  
		array_push($response["recipes"], $product);
	 }
}
 
// echoing JSON response
echo json_encode($response);
?>