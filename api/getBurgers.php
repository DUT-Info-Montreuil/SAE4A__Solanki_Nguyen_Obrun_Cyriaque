<?php
header('Content-Type: application/json');
include('pdo.php');
    $requete = $pdo->prepare("SELECT burger.id_burger, COALESCE(reduction, 0), burgername, price,photo ,description FROM `offer` RIGHT JOIN `burger` ON (offer.id_burger = burger.id_burger) ");
    $requete->execute();
    $result = $requete->fetchAll();
    if($result) {
        reponse_json(true, "Voici les burger",$result);
    } else {
        reponse_json(false, "Aucun Buger");
    }

?>
