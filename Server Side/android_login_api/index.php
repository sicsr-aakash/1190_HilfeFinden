<?php

if (isset($_POST['tag']) && $_POST['tag'] != '') {

    $tag = $_POST['tag'];


    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    if ($tag == 'login') {
        $email = $_POST['email'];
        $password = $_POST['password'];

        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            $response["success"] = 1;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } 
	else if ($tag == 'interest')
	{
	$interestemail = $_POST['email'];
	$interest = $_POST['interest'];
	$user = $db->storeInterest($interestemail, $interest);
	if ($user) {
                $response["success"] = 1;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Adding Interest.. :) ";
                echo json_encode($response);
            }
	
	}

	else if ($tag == 'search')
	{
	$email = $_POST['email'];
	$selectedFromList = $_POST['selectedFromList'];
	$user = $db->searchInterest($email, $selectedFromList);
	if ($user) {
                $response["success"] = 1;
		$response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["mobile"] = $user["mobile"];
            $response["user"]["distance"] = $user["distance"];
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Kuch toh gadbad hai... :) ";
                echo json_encode($response);
            }
	
	}
else if ($tag == 'register') {
        $name = $_POST['name'];
        $email = $_POST['email'];
	$mobile = $_POST['mobNo'];
	$lat	= $_POST['latitude'];
	$long	= $_POST['longitude'];
        $password = $_POST['password'];

        if ($db->isUserExisted($email)) {
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            $user = $db->storeUser($name, $email, $password,$mobile,$lat,$long);
            if ($user) {
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>
