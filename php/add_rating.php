<?php

$recipe_id = $_POST['recipe_id'];
$rating = $_POST['rating'];

// include db connect class
require_once dirname(__FILE__) . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$request = mysql_query("SELECT rating,rating_counter FROM Recipe WHERE id='$recipe_id';");
$row = mysql_fetch_array($request);
$old_rating = $row["rating"];
$counter = $row["rating_counter"];

$new_rating = ($old_rating * $counter + $rating)/ ($counter + 1);
$new_rating = number_format($new_rating, 2);

$result = mysql_query("UPDATE Recipe SET rating = '$new_rating', rating_counter = rating_counter + 1 WHERE id='$recipe_id';");

$counter = $counter + 1;
$new_top_param = $counter/($counter+10)*$new_rating+(10/($counter+10))*3.5;

$result = mysql_query("UPDATE Recipe SET top_param = '$new_top_param' WHERE id='$recipe_id';");

echo round($new_rating);
?>