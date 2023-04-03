<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['idUser']){
        $idUser = $_POST['idUser'];
        $requete = $pdo->prepare("DELETE FROM `cart` WHERE id_user= ?;");
        $requete->execute(array($idUser));
    if($result) {
        reponse_json(true, "Voici les burger en promotion",$result);
    } else {
        reponse_json(false, "Aucun Burger");
    }
    }else{
        reponse_json(false, "Erreur");
    }

    

?>
