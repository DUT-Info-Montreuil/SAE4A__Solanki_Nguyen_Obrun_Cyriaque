<?php

$hote = 'localhost';
$port = "3306";
$nom_bdd = 'api';
$utilisateur = 'root';
$mot_de_passe ='';
$retour;
try {
	//On test la connexion à la base de donnée
    $pdo = new PDO('mysql:host='.$hote.';port='.$port.';dbname='.$nom_bdd, $utilisateur, $mot_de_passe);
	$retour["succes"]= true;
	$retour["msg"]= 'Connexioon à la  base de donnée réussie';
} catch(Exception $e) {
	retour_jsn(false,"Connexion à la base de donnée impossible");

}

?>