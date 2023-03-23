<?php
header('Content-Type: application/json');
include('pdo.php');

         // Requête SQL pour insérer l'utilisateur dans la base de données
        
         

if(null!=$_POST['username'] && null!=$_POST['password'] && null!=$_POST['email'] && null!= $_POST['name']&& null!= $_POST['firstName']&& null!= $_POST['city'] && null!= $_POST['address']) {
    
    $email = $_POST["email"];
    $mdp = $_POST["password"];
    $username = $_POST["username"];  
    $name = $_POST['name'];
    $firstName = $_POST['firstName'];
    $city = $_POST['city'];
    $address = $_POST['address'];


    $requete = $pdo->prepare("SELECT count(username) FROM `user` where username = ? ;");
    $requete->execute(array($username));
    $result = $requete->fetch();

    if($result["count(username)"] == 1) {
    reponse_json(false, "User name existant");
    } else {
        $requete = $pdo->prepare("SELECT count(email) FROM `user` where email = ? ;");
        $requete->execute(array($email));
        $result = $requete->fetch();
        if($result["count(email)"] == 1) {
            reponse_json(false, "email déja existant");
        } else {
            try {
                // Requête SQL pour insérer l'utilisateur dans la base de données
                $requete = $pdo->prepare("INSERT INTO `user` (`id_user`, `username`, `password`, `email`, `address`, `city`, `photo`, `name`, `firstname`) VALUES (NULL, ?, ?, ?,?,?,?,?,?);");
                $requete->execute(array($username,$mdp, $email,$address,$city,"test.png",$name,$firstName));
                reponse_json(true, "Vous êtes bien inscrits");
            } catch(Exception $e) {
                reponse_json(false, "Connexion à la base de données impossible");

            }
        }
    }
}
?>
