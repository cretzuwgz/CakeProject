<?php 

$user_id = $_POST['user_id'];
 
 if(empty($user_id)){
	echo "FAIL";
	return 1;
}
 
// array for JSON response
$response = array();
 
// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT * FROM Recipe WHERE uploader='$user_id' order by date DESC;");

$response["recipes"] = array(); 
if($result){
	while($row = mysql_fetch_array($result))
	{
		$product = array();
		$product["id"] = $row["id"];
		$product["title"] = $row["title"];
		$product["date"] = $row["date"];
		
		$uploaderID = $row["uploader"];
		$up_response = mysql_query("SELECT username FROM User where id ='$uploaderID';");
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
		$tag_result = mysql_query("SELECT * FROM Has_Tag join Tag on(id_tag = id) where id_recipe = '$id';");
		if($tag_result){
			while($tag_row = mysql_fetch_array($tag_result)){
				array_push($product["tags"], $tag_row["name"]);
			}
		}
		
		$product["ingredients"] = array();
		$tag_result = mysql_query("SELECT * FROM Req_Ingredient join Ingredient on(id_ingredient = id) where id_recipe = '$id';");
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