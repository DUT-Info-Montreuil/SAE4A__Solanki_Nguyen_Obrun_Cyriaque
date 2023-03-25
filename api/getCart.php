<?php
header('Content-Type: application/json');
include('pdo.php');
    $requete = $pdo->prepare("SELECT * FROM burger NATURAL JOIN cart WHERE id_user=?");
    $requete->execute(array($_POST['id_user']));
    $result = $requete->fetchAll();
    if($result) {
        reponse_json(true, "Voici les burger de votre panier",$result);
    } else {
        reponse_json(false, "Aucun Buger dans le panier");
    }

?>
