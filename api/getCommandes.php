<?php
header('Content-Type: application/json');
include('pdo.php');
    $requete = $pdo->prepare("SELECT id_commande, burgername, modification, pret FROM `commande` INNER JOIN burger on burger.id_burger = commande.id_burger");
    $requete->execute();
    $result = $requete->fetchAll();
    if($result) {
        reponse_json(true, "Voici les burger",$result);
    } else {
        reponse_json(false, "Aucun Buger");
    }

?>
