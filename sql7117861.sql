-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- Värd: sql7.freesqldatabase.com
-- Skapad: 04 maj 2016 kl 09:47
-- Serverversion: 5.5.47-0ubuntu0.14.04.1
-- PHP-version: 5.3.28

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Databas: `sql7117861`
--

-- --------------------------------------------------------

--
-- Tabellstruktur `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `description` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

--
-- Dumpning av Data i tabell `category`
--

INSERT INTO `category` (`id`, `name`, `description`) VALUES
(1, 'pistols', 'Pistols from everywhere in the galaxy!'),
(2, 'robots', 'Robots can help you in everything'),
(3, 'stuff', 'all kind of stuff');

-- --------------------------------------------------------

--
-- Tabellstruktur `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderStatus` int(3) NOT NULL COMMENT '1 - new\n2 - shipped\n3 - delivered',
  `products` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'product id''s listed seperated with a semicolon',
  `price` int(11) NOT NULL,
  `date` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=3 ;

--
-- Dumpning av Data i tabell `orders`
--

INSERT INTO `orders` (`id`, `orderStatus`, `products`, `price`, `date`) VALUES
(1, 1, '1:1;2:1;', 1500, '19/04/16'),
(2, 2, '2:2;3:10;', 5000, '19/04/16');

-- --------------------------------------------------------

--
-- Tabellstruktur `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `category` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `image` varchar(400) COLLATE utf8_bin DEFAULT NULL,
  `description` varchar(800) COLLATE utf8_bin NOT NULL,
  `shortDescription` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=4 ;

--
-- Dumpning av Data i tabell `product`
--

INSERT INTO `product` (`id`, `name`, `category`, `price`, `image`, `description`, `shortDescription`, `quantity`) VALUES
(1, 'DL-18 Blaster Pistol', 1, 500, 'http://cgcookie.com/app/uploads/2015/01/dl18_render_test_3edit1-1024x576.png', 'These compact blaster pistols were favored by elements of the underworld, particularly those based on Tatooine. In the early days of the Galactic Civil War, former Jedi Kanan Jarrus carried one, using it far more often than his secret lightsaber. Later in the war, DL-18s were in wide distribution inside Jabba’s Palace in the last days of his life. Luke Skywalker used the Force to snatch one from one of Jabba’s men, just before he fell into the rancor pit below Jabba’s throne.', 'What a pistol!', 2),
(2, 'R5-series', 2, 1000, 'resources/img/sample2.png', 'The R5-series astromech droid was a line of low cost astromech droids built by Industrial Automaton. Based upon the success of prior astromech models, such as the wildly popular R2-series, Industrial Automaton intended the R5-series to cater to budget buyers at the cost of some functionality.', '    "A meter-tall stack of the worst business decisions you could possibly want." \n    ―Mechtech Illustrated, criticizing the R5-series', 20),
(3, 'Holster Model A', 3, 300, 'resources/img/sample3.jpg', 'A quick-draw holster was a special type of holster designed to allow an individual to very rapidly draw their blaster pistol. This was partly achieved by removing the strap used to secure the blaster in place which was found on common holsters. The notorious bounty hunter Cad Bane had dual quick-draw holsters for his twin modified LL-30 blaster pistols. ', 'Can hold everything you need!', 30);

-- --------------------------------------------------------

--
-- Tabellstruktur `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `bucket` text COLLATE utf8_bin NOT NULL,
  `orders` text COLLATE utf8_bin NOT NULL,
  `email` varchar(200) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
