<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['idBurger']){
        $requete = $pdo->prepare("SELECT * FROM `burger` WHERE id_burger= ?;");
        $requete->execute(array($idBurger));
        $result = $requete->fetch();


    if($result) {
        reponse_json(true, "Voici les burger en promotion",$result);
    } else {
        reponse_json(false, "Aucun Burger");
    }
    }

    

?>
