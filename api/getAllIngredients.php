<?php
header('Content-Type: application/json');
include('pdo.php');



    $requete = $pdo->prepare("SELECT * FROM `ingredient` ");
    $requete->execute();
    $result=$requete->fetchAll();
               
    if($result)
        reponse_json(true, "all ingredients", $result); 
    else
        reponse_json(false, "aucun ingredient"); 

    

?>
