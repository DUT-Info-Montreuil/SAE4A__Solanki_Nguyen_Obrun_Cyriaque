<?php
header('Content-Type: application/json');
include('pdo.php');




if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST["username"];
    $mdp = $_POST["password"];
    
    $requete = $pdo->prepare("SELECT * FROM `user` where username = ? ;");
    $requete->execute(array($username));
    $result = $requete->fetch();
    if($result) {
        if($mdp==$result['password']) {
            reponse_json(true, "Connexion réussie", $result);
        } else {
            reponse_json(false, "Mot de passe incorrect");
        }
    } else {
        reponse_json(false, "Aucun compte n'existe pour ce nom d'utilisateur");
    }
} else {
    reponse_json(false, "Méthode non autorisée");
}
?>
