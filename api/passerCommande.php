<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['id_burger'] && null!=$_POST['modification']){
        $idBurger = $_POST['id_burger'];
        $modification = $_POST['modification'];
        $requete = $pdo->prepare("INSERT INTO `commande`(`id_commande`, `id_burger`, `modification`, `pret`) VALUES (?,?,?,?)");
        $requete->execute(array(null,$idBurger, $modification, true));
        reponse_json(false, "Commande passer avec succes");


    }else {
        reponse_json(false, "Aucune commande");
    }

    

?>
