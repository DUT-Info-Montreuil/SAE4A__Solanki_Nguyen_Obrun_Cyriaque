<?php
header('Content-Type: application/json');
include('pdo.php');
    $requete = $pdo->prepare("SELECT * FROM `buger`  ;");
    $requete->execute();
    $result = $requete->fetchAll();
    if($result) {
        reponse_json(true, "Voici les burger",$result);
    } else {
        reponse_json(false, "Aucun Buger");
    }

?>
