<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['idCommande']){
        $idCommande = $_POST['idCommande'];
        $requete = $pdo->prepare("UPDATE commande set pret=1 where id_commande = ?");
        $requete->execute(array($idCommande));


    }else {
        reponse_json(false, "Aucune commande");
    }

    

?>
