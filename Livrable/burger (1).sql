-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : jeu. 13 avr. 2023 à 21:34
-- Version du serveur :  10.5.16-MariaDB
-- Version de PHP : 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `id20484514_api`
--

-- --------------------------------------------------------

--
-- Structure de la table `burger`
--

CREATE TABLE `burger` (
  `id_burger` bigint(20) NOT NULL,
  `burgername` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `photo` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(3000) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `burger`
--

INSERT INTO `burger` (`id_burger`, `burgername`, `price`, `photo`, `description`) VALUES
(1, 'lovver', 25, 'lovver', 'Le burger \"Cheddar Lover\" est un délicieux hamburger garni d\'un généreux morceau de fromage cheddar fondant, accompagné de laitue croquante, de tomates fraîches et d\'une sauce spéciale. Le tout est servi sur un pain moelleux et grillé pour une expérience culinaire savoureuse et satisfaisante.'),
(25, 'Yamani', 15, '', 'Le burger El Yamani est une délicieuse combinaison de saveurs orientales et occidentales. Ce burger est garni d\'un steak haché juteux, d\'une sauce épicée à base de cumin et de coriandre, et de tranches de concombre croquantes. Le tout est servi sur un pain brioché moelleux, accompagné d\'une généreuse portion de frites croustillantes. Ce burger est un véritable festin pour les amateurs de cuisine fusion.'),
(26, 'Akyoub', 12, '', 'Le burger est composé d\'un steak ou d\'un filet de poulet grillé, selon les préférences, garni de fromage fondu fondant et de jalapenos épicés pour un peu de mordant. Il est ensuite nappé d\'une mayonnaise légèrement épicée pour un peu plus de saveur et de texture. Le tout est servi sur un pain à burger grillé pour une expérience de dégustation riche et satisfaisante.'),
(27, 'SteveSteve', 8, '', 'stevesteve'),
(28, 'los pollos hermanos', 15, '', 'Le Los Pollos Hermano est un burger délicieusement épicé inspiré de la cuisine tex-mex. Composé d\'un steak haché juteux, d\'une tranche de fromage cheddar fondu, de jalapeños marinés et d\'une sauce piquante maison, le tout enveloppé dans un pain à burger grillé. Ce burger est un véritable festin pour les amateurs de saveurs audacieuses et de sensations fortes en bouche.'),
(29, 'YankPri', 10, '', 'yankPri'),
(30, 'Emiillii', 5, '', 'emiillii'),
(31, 'MarcheWan', 13, '', 'marchewan'),
(32, 'NoufeNouf', 15, '', 'noufenouf');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `burger`
--
ALTER TABLE `burger`
  ADD PRIMARY KEY (`id_burger`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `burger`
--
ALTER TABLE `burger`
  MODIFY `id_burger` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
