<?php

require_once('pdo.php');

$email=$_POST["email"];
$mdp=$_POST["password"];
$username=$_POST["username"];

// Requête SQL pour insérer l'utilisateur dans la base de données
$requete=$pdo->prepare(" INSERT INTO `user` (`id_user`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?);");
$requete->execute(array($username,password_hash($mdp,PASSWORD_ARGON2I),$email));
$succes= true;
$msg= 'utilisateur ajouter';
// Exécution de la requête SQL

    // Envoi d'une réponse JSON contenant un message de succès
    $response["status"] = "success";
    $response["message"] = "User registered successfully";
    echo json_encode($response);





/*

var_dump($_POST);

if(!empty($_POST["email"]) && !empty($_POST["username"]) && !empty($_POST["password"])){
    $email=$_POST["email"];
    $mdp=$_POST["password"];
    $username=$_POST["username"];
    $requete=$pdo->prepare(" INSERT INTO `user` (`id_user`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?);");
    $requete->execute(array($username,password_hash($mdp,PASSWORD_ARGON2I),$email));
    $succes= true;
	$msg= 'utilisateur ajouter';
    $restult=array();
    retour_jsn($succes,$msg,$restult);
}else{
    $succes= false;
	$msg= 'Il manque des infos';
    retour_jsn($succes,$msg);
}
/* 
	
	$requete->execute();
	$result=$requete->fetchAll();
	$retour["succes"]= true;
	$retour["msg"]= 'voici les utilisateurs';
	$retour["result"]["user"]=$result;
*/
?>

