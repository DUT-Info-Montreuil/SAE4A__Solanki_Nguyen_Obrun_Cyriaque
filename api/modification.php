<?php
header('Content-Type: application/json');
include('pdo.php');

         // Requête SQL pour insérer l'utilisateur dans la base de données
        
         

if(null!=$_POST['username']  ||  null!=$_POST['newpass']  || null!= $_POST['name'] || null!= $_POST['firstName'] || null!= $_POST['city'] || null!= $_POST['address'] || null!=$_POST['oldpass'] || null!=$_POST['newCpass']) {
    
    $username = $_POST["username"];
    $newpass = $_POST["newpass"];
    $newCpass = $_POST["newCpass"];
    $oldpass = $_POST["oldpass"]; 
    $name = $_POST['name'];
    $firstName = $_POST['firstName'];
    $city = $_POST['city'];
    $address = $_POST['address'];

    $erreur;

    

    try {
        $requete = $pdo->prepare("UPDATE user SET name = ?, firstname = ?, city = ?, address = ? WHERE username = ?; ");
        $requete->execute(array($name,$firstName, $city,$address,$username));
        $erreur = true;
    } catch(Exception $e) {
        $erreur = false;
        reponse_json(false, "Connexion à la base de données impossible");
    }

    if(!empty($oldpass)){
        if($newCpass==$newpass){
            $requetemdp = $pdo->prepare("SELECT `password` from user where username=?");
            $requetemdp->execute(array($username));
            $result = $requetemdp->fetch();
            $password = $result['password'];
    
            if (password_verify($oldpass, $result['password'])) {
                try {
                    $requete = $pdo->prepare("UPDATE user SET password = ? WHERE username = ?; ");
                    $requete->execute(array(password_hash($newpass,PASSWORD_ARGON2I),$username));
                    $erreur = true;
                } catch(Exception $e) {
                    $erreur = false;
                    reponse_json(false, "Connexion à la base de données impossible");
                }
            } else {
                $erreur = false;
                reponse_json(false, "Ancien mot de passe incorrect . $oldpass. $password");
            }
        }else{
            $erreur = false;
            reponse_json(false, "La confirmation de mot de passe ne correspond pas au mot de passe");
        }
    }
    if($erreur)
        reponse_json(true, "Votre profil à bien été modifié");
}
else{
    reponse_json(true, "Aucune modification");
}
?>
