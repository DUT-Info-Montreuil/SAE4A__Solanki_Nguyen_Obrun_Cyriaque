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
	reponse_json(false,"Connexion à la base de donnée impossible");

}



function reponse_json($success, $msgErreur, $data=NULL) {
    $array['success'] = $success;
    $array['msg'] = $msgErreur;
    $array['result'] = $data;

    echo json_encode($array);
}

?>