<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['idBurger']){
        $idBurger = $_POST['idBurger'];
        $requete = $pdo->prepare("SELECT * FROM `contains` WHERE id_burger= ?;");
        $requete->execute(array($idBurger));
        $result = $requete->fetch();


    if($result) {
        reponse_json(true, "Voici les ingredients du burger",$result);
    } else {
        reponse_json(false, "Aucun Burger");
    }
    }

    

?>
