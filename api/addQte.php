<?php
header('Content-Type: application/json');
include('pdo.php');


if(null!=$_POST['id_user'] && null!=$_POST['id_burger'] ) {  
                $requete = $pdo->prepare("SELECT * FROM `cart` WHERE `id_user`=? AND `id_burger`=?;");
                $requete->execute(array($_POST['id_user'],$_POST['id_burger']));
                $row=$requete->fetch();
                $qte=$row['quantity']+1;
                
                $requete = $pdo->prepare("UPDATE `cart` SET `quantity` = $qte WHERE `cart`.`id_user` = ? AND `cart`.`id_burger` = ?");
                $requete->execute(array($_POST['id_user'],$_POST['id_burger']));
                $row=$requete->fetch();
                reponse_json(true, "Burger ajouter au panier");   
}
    

?>
