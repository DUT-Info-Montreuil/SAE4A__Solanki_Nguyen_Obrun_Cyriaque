<?php
header('Content-Type: application/json');
include('pdo.php');
    $requete = $pdo->prepare("DELETE from commande where pret=1");
    $requete->execute();


?>