<?php
header('Content-Type: application/json');
include('pdo.php');

         // Requête SQL pour insérer l'utilisateur dans la base de données
        
         

if( null!=$_POST['newpass']  && null!= $_POST['name']&& null!= $_POST['firstName']&& null!= $_POST['city'] && null!= $_POST['address'] && null!=$_POST['oldpass'] && null!=$_POST['newCpass']) {
    
    $newpass = $_POST["newpass"];
    $newCpass = $_POST["newCpass"];
    $oldpass = $_POST["oldpass"]; 
    $name = $_POST['name'];
    $firstName = $_POST['firstName'];
    $city = $_POST['city'];
    $address = $_POST['address'];

    try {
        // Requête SQL pour insérer l'utilisateur dans la base de données
        $requete = $pdo->prepare("UPDATE user SET username = ?, nom_colonne_2 = ? WHERE condition; ");
        $requete->execute(array($username,$mdp, $email,$address,$city,"test.png",$name,$firstName));
        reponse_json(true, "Votre profil à été modifié");
    } catch(Exception $e) {
        reponse_json(false, "Connexion à la base de données impossible");
    }
        
    
}
?>
