<?php
header('Content-Type: application/json');
include('pdo.php');

    if(null!=$_POST['nameIngr'] && null!=$_POST['nameBurger'] && null!=$_POST['position']){
        $burgerName = $_POST['nameBurger'];
        $requete = $pdo->prepare("SELECT id_burger FROM `burger` WHERE burgername= ?;");
        $requete->execute(array($burgerName));
        $result = $requete->fetch();

        $ingrName = $_POST['nameIngr'];
        $requetea = $pdo->prepare("SELECT id_ingr FROM `ingredient` WHERE ingrname= ?;");
        $requetea->execute(array($ingrName));
        $resulta = $requetea->fetch();

        $idIngr = $resulta['id_ingr'];
        $idBurger = $result['id_burger'];
        $position = $_POST['position'];

        $requeteb = $pdo->prepare("INSERT INTO `contains`(`id_burger`, `id_ingr`, `position`) VALUES (?,?,?)");
        $requeteb->execute(array($idBurger,$idIngr,$position));

        


        if($result) {
            reponse_json(true, "ingredient ajouté",$result);
        } else {
            reponse_json(false, "erreur");
        }
    }else{
        
    }

    

?>
