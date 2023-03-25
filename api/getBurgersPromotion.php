<?php
header('Content-Type: application/json');
include('pdo.php');


    $requete = $pdo->prepare("SELECT * FROM `offer` INNER JOIN `burger` ON (offer.id_burger = burger.id_burger);");
    $requete->execute();
    $result = $requete->fetchAll();


    if($result) {
        reponse_json(true, "Voici les burger en promotion",$result);
    } else {
        reponse_json(false, "Aucun Burger");
    }

?>
