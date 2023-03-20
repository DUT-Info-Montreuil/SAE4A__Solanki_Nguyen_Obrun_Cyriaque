<?php
header('Content-Type: application/json');
include('pdo.php');

function reponse_json($success, $msgErreur, $data=NULL) {
	$array['success'] = $success;
	$array['msg'] = $msgErreur;
	$array['result'] = $data;

	echo json_encode($array);
}
$email = $_POST["email"];
$mdp = $_POST["password"];
$username = $_POST["username"];  

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
            $requete = $pdo->prepare("INSERT INTO `user` (`id_user`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?);");
            $requete->execute(array($username,$mdp, $email));

            reponse_json(true, "Vous êtes bien inscrits");
        } catch(Exception $e) {
            reponse_json(false, "Connexion à la base de données impossible");

        }
    }
}
?>
