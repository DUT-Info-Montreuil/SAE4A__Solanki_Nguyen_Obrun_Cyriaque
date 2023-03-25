<?php
header('Content-Type: application/json');
include('pdo.php');


if(null!=$_POST['id_user'] && null!=$_POST['id_burger'] ) {  
           try {
            $requete = $pdo->prepare("SELECT * FROM `cart` WHERE `id_user`=? AND `id_burger`=?;");
            $requete->execute(array($_POST['id_user'],$_POST['id_burger']));
            $row=$requete->fetch();
            
            if($row['id_user']>0){
                $requete = $pdo->prepare("UPDATE `cart` SET `quantity` = $row['quantity']+1 WHERE `cart`.`id_user` = ? AND `cart`.`id_burger` = ?");
                $requete->execute(array($_POST['id_user'],$_POST['id_burger']));
                $row=$requete->fetch();
                reponse_json(true, "Burger ajouter au panier");
            }else{
                $requete = $pdo->prepare("INSERT INTO `cart` (`id_user`, `id_burger`, `quantity`) VALUES (?,?,'1');");
                $requete->execute(array($_POST['id_user'],$_POST['id_burger']));
            }        
             } catch(Exception $e) {
             reponse_json(false, "Connexion à la base de données impossible");
             }
}
    

?>
