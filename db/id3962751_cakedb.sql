-- phpMyAdmin SQL Dump
-- version 4.7.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 20, 2017 at 10:29 AM
-- Server version: 10.1.29-MariaDB
-- PHP Version: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id3962751_cakedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `Favorite_Tag`
--

CREATE TABLE `Favorite_Tag` (
  `id_user` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Favorite_Tag`
--

INSERT INTO `Favorite_Tag` (`id_user`, `id_tag`) VALUES
(1, 7),
(3, 1),
(3, 2),
(3, 12),
(3, 16),
(4, 7),
(4, 17),
(4, 23),
(4, 24),
(5, 7),
(5, 23);

-- --------------------------------------------------------

--
-- Table structure for table `Has_Favorite`
--

CREATE TABLE `Has_Favorite` (
  `id_user` int(11) NOT NULL,
  `id_recipe` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Has_Favorite`
--

INSERT INTO `Has_Favorite` (`id_user`, `id_recipe`) VALUES
(3, 11),
(4, 3),
(5, 11);

-- --------------------------------------------------------

--
-- Table structure for table `Has_Tag`
--

CREATE TABLE `Has_Tag` (
  `id_recipe` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Has_Tag`
--

INSERT INTO `Has_Tag` (`id_recipe`, `id_tag`) VALUES
(0, 10),
(0, 14),
(0, 15),
(0, 16),
(2, 7),
(2, 8),
(2, 9),
(3, 10),
(3, 11),
(3, 12),
(4, 9),
(4, 13),
(6, 9),
(6, 10),
(6, 17),
(7, 17),
(7, 18),
(7, 19),
(8, 10),
(8, 17),
(8, 20),
(8, 21),
(9, 17),
(9, 22),
(10, 12),
(10, 16),
(10, 20),
(10, 21),
(11, 7),
(11, 9),
(11, 23);

-- --------------------------------------------------------

--
-- Table structure for table `Ingredient`
--

CREATE TABLE `Ingredient` (
  `id` int(11) NOT NULL,
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `measurement` varchar(15) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Ingredient`
--

INSERT INTO `Ingredient` (`id`, `name`, `measurement`) VALUES
(1, 'asd', 'kg'),
(2, 'ou', 'bucata'),
(3, 'esenta de vanilie', 'bucata'),
(4, 'zahar', 'grame'),
(5, 'unt moale', 'grame'),
(6, 'faina', 'grame'),
(7, 'lapte caldut', 'mililitri'),
(8, 'piscoturi', 'pachete'),
(9, 'oua', 'bucata'),
(10, 'mascarpone', 'g'),
(11, 'frisca lichida', 'g'),
(12, 'cafea', 'ml'),
(13, 'zahar vanilat', 'plic'),
(14, 'esenta rom', 'lingurita'),
(15, 'unt125', ''),
(16, 'lamaie', 'bucata'),
(17, 'galbenus', 'buc'),
(18, 'suc de mere', 'linguri'),
(19, 'faina de migdale', 'g'),
(20, 'migdale feliate', 'g'),
(21, 'mere golden', 'buc'),
(22, 'napolitana', 'pachet'),
(23, 'biscuiti', 'g'),
(24, 'nuca macinata', 'g'),
(25, 'zahar tos', 'g'),
(26, 'cacao', 'lingura'),
(27, 'lapte', 'ml'),
(28, 'esenta rom', 'fiola'),
(29, 'ciocolata alba', 'g'),
(30, 'sirop capsune', 'linguri'),
(31, 'ulei', 'ml'),
(32, 'miere', 'lingura'),
(33, 'amoniac', 'pliculete'),
(34, 'otet', 'lingura'),
(35, 'apa', 'ml'),
(36, 'coaza lamaie', 'g'),
(37, 'nuca cocos', 'plc'),
(38, 'apa', 'linguri'),
(39, 'cacao', 'linguri'),
(40, 'esenta rom', 'lingurite'),
(41, 'cafea tare', 'linguri'),
(42, 'praf copt', 'plc'),
(43, 'cafea solubila', 'linguri'),
(44, 'zahar pudra', 'g'),
(45, 'rom', 'ml'),
(46, 'miere', 'linguri'),
(47, 'biscuiti cu cacao', 'g'),
(48, 'crema de branza', 'g'),
(49, 'smantana', 'ml'),
(50, 'gelatina', 'plic'),
(51, 'fulgi de ciocolata', 'g'),
(52, 'praf de copt', 'lingurita'),
(53, 'unt topit', 'g'),
(54, 'zahar pudra vanilat', 'linguri'),
(55, 'smantana', 'g'),
(56, 'crema fina branza', 'g'),
(57, 'esenta de vanilie', 'fiola'),
(58, 'esenta de vanilie', 'plic'),
(59, 'lapte caldut', 'ml'),
(60, 'suc de lamaie', 'ml'),
(61, 'coaja razuita de lamaie', 'bucata'),
(62, 'vanilie', 'g'),
(63, 'spanac', 'kg');

-- --------------------------------------------------------

--
-- Table structure for table `Recipe`
--

CREATE TABLE `Recipe` (
  `id` int(11) NOT NULL,
  `title` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `uploader` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `p_link` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rating` float NOT NULL DEFAULT '0',
  `rating_counter` int(11) NOT NULL DEFAULT '0',
  `difficulty` int(11) DEFAULT NULL,
  `req_time` int(11) DEFAULT NULL,
  `searched` int(11) NOT NULL DEFAULT '0',
  `accessed` int(11) NOT NULL DEFAULT '0',
  `top_param` double NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Recipe`
--

INSERT INTO `Recipe` (`id`, `title`, `date`, `uploader`, `description`, `p_link`, `rating`, `rating_counter`, `difficulty`, `req_time`, `searched`, `accessed`, `top_param`) VALUES
(2, 'Prajitura inteligenta', '2017-12-14 07:03:41', '1', '1. Preincalzeste cuptorul la 160 grade si unge cu unt sau margarina o tava de prajituri patrata cu dimensiuni de 20/20 cm, sau una rotunda. Ai grija sa alegi o tava nu foarte mare in latime sau lungime, deoarece aluatul trebuie sa fie suficient de inalt incat sa se poata stratifica.\r\n\r\n2. Separa albusurile de galbenusuri si bate albusurile, pana cand spuma ramane pe paletele mixerului.\r\n\r\n3. Freaca galbenusurile cu zaharul pana devin o crema usoara. Adauga untul si esenta de vanilie, continua sa amesteci pentru 1-2 minute, apoi adauga si incorporeaza faina.\r\n\r\n4. Adauga laptele treptat, pana la omogenizare, urmat de albusurile batute, adaugate tot treptat, pe care trebuie sa le incorporezi delicat, folosind o spatula.\r\n\r\n5. Toarna aluatul in tava de prajitura si coace-o aproximativ o ora, sau pana cand stratul superior devine auriu, iar prajitura trece testul scobitorii.\r\n\r\n6. Orneaza prajitura cu zahar pudra.', 'http://www.retetepractice.ro/wp-content/uploads/2015/09/prajitura-inteligenta1.jpg', 0, 0, 1, 75, 0, 1, 0),
(3, 'Tiramisu rapid', '2017-12-14 07:13:17', '1', 'Bate galbenusurile cu zaharul pana devin spuma.\nSeparat, bate albusurile spuma, apoi incorporeaza frisca si continua sa mixezi.\nGalbenusurile se amesteca cu mascarpone, se adauga compozitia de albusuri cu frisca, urmata de esenta si zaharul vanilat.\nDa piscoturile prin cafeaua usor racita, intinde in tava un rand de piscoturi si peste pune crema obtinuta.\nContinua sa adaugi randuri de piscoturi alternate de crema, iar la suprafata trebuie sa ramana un ultim strat de crema.\nPudreaza tiramisu cu cacao. Da prajitura la frigider timp de 2-3 ore, apoi serveste-o.', 'http://www.retetepractice.ro/wp-content/uploads/2015/11/tiramisu-rapid.jpg', 4, 2, 1, 30, 0, 5, 3.5833333333333),
(4, 'Prajitura cu mere', '2017-12-14 07:20:22', '1', 'Pasul 1:\nIntr-un vas, se pun faina si untul, zaharul, coaja de lamaie si galbenusul. Se amesteca si apoi se toarna sucul de mere. Se amesteca pentru a obtine un aluat omogen. Se presara faina pe masa de lucru si se intinde  aluatul in functie de dimensiunea tavii de copt.\nPasul 2:\nSe transfera in tava de copt avand grija ca aluatul sa urce pe peretii vasului. Se da la frigider. Se curata merele si se taie in patru si apoi fiecare sfert se feliaza fara a desprinde complet. Trebuie sa arate ca un evantai.\nPasul 3:\nPentru crema de migdale se preseaza de suc jumatate de lamaie, care se bate usor cu oul si galbenusul. Migdalele feliate se maruntesc cu un sucitor. Se amesteca untul moale cu zaharul pentru a obtine o crema spumoasa in care se adauga ouale batute, migdalele usor faramitate si cele doua tipuri de faina. Se intinde crema in forma de copt, peste aluat, egalizand-o.\nPasul 4:\nSe asaza merele deasupra cremei cu migdale. Prajitura se da la cuptor pentru o ora. Imediat cum se scoate din cuptor, se unge cu gem incalzit.\n', 'http://www.retetepractice.ro/wp-content/uploads/2015/10/Prajitura-cu-mere.png', 0, 0, 3, 90, 0, 0, 0),
(5, 'Prajitura cu foi de napolitana si crema cu nuci si', '2017-12-14 07:24:40', '1', 'Se fierbe laptele cu zaharul pana clocoteste, apoi se ia de pe foc si se adauga in el untul taiat bucatele, nuca macinata si cele 5 linguri cu cacao. Se adauga apoi in crema biscuitii sfaramati si esenta de rom.\nCrema se intinde intre foile de napolitana. Prajitura se glazureaza cu ciocolata alba topita la bain-marie si se orneaza cu siropul de capsune.', 'http://www.retetepractice.ro/wp-content/uploads/2015/10/Prajitura-cu-foi-de-napolitana-si-crema-cu-nuci-si-biscuiti.png', 0, 0, 1, 30, 0, 0, 0),
(6, 'Prajitura tavalita', '2017-12-14 07:27:18', '1', 'Se pune o cantitate mica de faina intr-un vas si se amesteca impreuna cu amoniacul stins cu o?et. Se inglobeaza zaharul si zaharul vanilat, uleiul, mierea, apa si coaja de lamaie si se amesteca bine. La final, se adauga faina, amestecand, pana ce compozi?ia capata consisten?a unei smantani groase.\nAluatul ob?inut se toarna in tava tapetata cu hartie de copt si se da la cuptor pentru 35-40 de minute, pana trece testul cu scobitoarea. Se scoate blatul si se lasa la racit.\nIntre timp, se prepara glazura de ciocolata: se pun pe foc, mai intai, apa, uleiul si zaharul. Cand au dat in clocot, se adauga cacaua si romul si se amesteca bine, astfel incat sa nu existe cocoloase de cacao. Se da glazura de o parte si se lasa sa se raceasca.\nBlatul racorit se taie cuburi potrivite ca marime care se trec pe rand prin glazura, apoi se tavalesc prin nuca de cocos.', 'http://www.retetepractice.ro/wp-content/uploads/2015/10/Prajitura-cu-cocos-de-post.jpg', 0, 0, 1, 55, 0, 1, 0),
(7, 'Prajitura Creola', '2017-12-14 07:30:13', '1', 'Pentru blat, untul, zaharul, mierea si ouale se mixeaza.\nSe pun apoi faina, praful de copt si esenta de cafea. Se amesteca 2 minute, se toarna in tava usor unsa si se coace 30 de minute. Se lasa la racit, se taie si se imbiba cele doua blaturi cu sirop facut din fierberea apei cu zaharul.\nPentru crema, se pun pe foc cafeaua si mierea, cu romul. Ouale se freaca cu zaharul si, dupa ce se obtine o crema alba, se adauga untul si jumatate din mierea cu cafea. Se amesteca si se pune si restul amestecului de miere.\nCrema se asaza intre cele doua blaturi insiropate, se da la rece pentru 4 ore si se serveste cu ciocolata rasa deasupra si boabe de cafea.', 'http://www.retetepractice.ro/wp-content/uploads/2015/10/Prajitura-creola.jpg', 0, 0, 1, 60, 0, 0, 0),
(8, 'Cheesecake cu ciocolata', '2017-12-14 07:31:56', '1', 'Macina biscuitii pana devin firimituri, adauga untul si amesteca bine.\nPreseaza amestecul de biscuiti si unt intr-o tava unsa, incat sa obtii blatul, apoi pune tava la frigider.\nBate crema de branza impreuna cu zaharul pana devine fina si omogena.\nDizolva gelatina in apa rece, apoi incalzeste-o la bain marie.\nTopeste ciocolata impreuna cu smantana pentru gatit.  Adauga amestecul peste crema de branza, urmata de gelatina dizolvata si amesteca bine.\nPune crema astfel obtinuta peste blat si uniformizeaza.\nServeste chesecake-ul cu ciocolata rasa pe deasupra.', 'http://www.retetepractice.ro/wp-content/uploads/2015/12/cheesecake-cu-ciocolata.jpg', 0, 0, 1, 25, 0, 0, 0),
(9, 'Negresa din 3 ingrediente', '2017-12-14 07:33:36', '1', 'Topeste fulgii de ciocolata sau ciocolata la microunde sau la bain marie.\nBate ouale timp de 6-7 minute pe viteza maxima a mixerului, pana cand ouale cresc mult in volum, apoi adauga si praful de copt.\nAdauga ciocolata in centrul amestecului de oua si continua sa mixezi pana la omogenizare (1-2 minute).\nToarna amestecul obtinut intr-o tava tapetata cu hartie de copt si da tava la cuptor pentru 30 de minute. Taie prajitura numai dupa ce se raceste.', 'https://www.youtube.com/watch?v=KxDIBYGPAiM', 0, 0, 1, 40, 0, 2, 0),
(10, 'Cheesecake New York', '2017-12-14 07:35:34', '1', 'Se prepara mai intai compozitia de biscuiti care va trebui sa stea la frigider. Se Zdrobesc biscuitii intr-o punga sau se maruntesc cu ajutorul robotului, apoi se amesteca bine cu zaharul pudra si untul topit.\nSe tapeteaza un vas cu hartie de copt sau doar se unge cu unt, apoi se preseaza bine compozitia de biscuiti pe fundul formei. Se da la frigider.\nPentru crema, se pune gelatina la hidratat in 50 ml apa rece pentru 5 minute, apoi se pune la foc mic pana se incalzeste si se dizolva.\nSmantana pentru frisca se mixeaza cu 100 g zahar si se bate spuma tare. Apoi, se adauga smantana, crema de branza, restul de zahar si esenta si se omogenizeaza bine totul.\nSe incorporeaza si gelatina si se toarna crema peste blatul de biscuiti.\nSe da desertul la rece pana a doua zi. Se poate servi decorat cu fructe proaspete, sos de fructe sau alte variante la alegere.', 'http://www.retetepractice.ro/wp-content/uploads/2015/10/Cheesecake-New-York-in-farfurie-alba-cu-lingurita-de-argint.jpg', 0, 0, 1, 60, 0, 2, 0),
(11, 'Prajitura inteligenta cu lamaie', '2017-12-14 07:40:07', '1', '1. Preincalzeste cuptorul la 160 grade si unge cu unt o tava patrata de 20/20 cm sau una rotunda. Alege o tava nu foarte mare ca latime sau lungime, deoarece daca nu este suficient de inalt aluatul in tava, exista riscul sa nu se poata stratifica.\n\n2. Separa albusurile de galbenusuri si bate albusurile spuma, pana cand aceasta ramane pe paletele mixerului, atunci cand il ridici\n\n3. Freaca galbenusurile cu zaharul pana obtii o consistenta cremoasa si usoara. Adauga untul si esenta de vanilie, amesteca pentru 1-2 minute, apoi adauga si incorporeaza faina.\n\n4. Adauga zeama si coaja de lamaie, apoi adauga laptele treptat, pana la omogenizare, urmat de albusurile batute.\n\n5. Albusurile trebuie amestecate cu o spatula, nu cu mixerul, si trebuie adaugate tot treptat, cat mai delicat.\n\n6. Nu trebuie sa omogenizezi aluatul, albusul trebuie sa ramana oarecum vizibil in compozitie, deci incorporeaza albusurile superficial.\n\n7. Toarna aluatul in tava de prajitura si da-o la cuptor pentru aproximativ o ora, sau pana ce se coloreaza auriu, iar prajitura trece testul scobitorii.\n\n8. Decoreaza prajitura cu zahar pudra.', 'http://www.retetepractice.ro/wp-content/uploads/2015/09/prajitura-inteligenta-cu-lamaie.jpg', 3.5, 6, 1, 75, 0, 16, 3.5);

-- --------------------------------------------------------

--
-- Table structure for table `Request`
--

CREATE TABLE `Request` (
  `id` int(11) NOT NULL,
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_user` int(11) NOT NULL,
  `p_link` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `votes` int(11) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Req_Ingredient`
--

CREATE TABLE `Req_Ingredient` (
  `quantity` int(11) NOT NULL,
  `id_recipe` int(11) NOT NULL,
  `id_ingredient` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Req_Ingredient`
--

INSERT INTO `Req_Ingredient` (`quantity`, `id_recipe`, `id_ingredient`) VALUES
(0, 4, 1),
(0, 4, 15),
(1, 0, 22),
(1, 0, 28),
(1, 2, 3),
(1, 3, 13),
(1, 3, 14),
(1, 4, 2),
(1, 4, 16),
(1, 4, 17),
(1, 6, 13),
(1, 6, 32),
(1, 6, 34),
(1, 6, 37),
(1, 7, 42),
(1, 8, 50),
(1, 9, 52),
(1, 10, 50),
(1, 10, 57),
(1, 11, 58),
(1, 11, 61),
(2, 0, 30),
(2, 3, 8),
(2, 4, 18),
(2, 6, 33),
(2, 7, 46),
(2, 10, 54),
(3, 6, 39),
(3, 6, 40),
(3, 7, 41),
(3, 7, 43),
(4, 2, 2),
(4, 4, 21),
(4, 7, 9),
(4, 9, 9),
(4, 11, 9),
(5, 0, 26),
(5, 3, 9),
(5, 6, 38),
(25, 4, 4),
(25, 4, 6),
(30, 7, 45),
(45, 4, 20),
(50, 6, 31),
(50, 6, 36),
(50, 11, 60),
(60, 7, 32),
(80, 4, 19),
(80, 8, 5),
(100, 7, 4),
(100, 7, 35),
(100, 8, 4),
(100, 8, 49),
(100, 10, 53),
(115, 2, 6),
(115, 11, 6),
(125, 2, 5),
(125, 4, 4),
(125, 4, 5),
(125, 11, 5),
(150, 2, 4),
(150, 3, 4),
(150, 6, 31),
(150, 7, 5),
(150, 7, 44),
(150, 8, 29),
(150, 11, 4),
(200, 3, 11),
(200, 3, 12),
(200, 7, 4),
(200, 9, 51),
(200, 10, 44),
(200, 10, 55),
(200, 10, 56),
(225, 4, 6),
(250, 0, 5),
(250, 0, 24),
(250, 0, 25),
(250, 0, 27),
(250, 3, 10),
(250, 6, 35),
(250, 7, 5),
(250, 7, 6),
(250, 8, 47),
(300, 0, 23),
(300, 0, 29),
(300, 6, 4),
(300, 10, 23),
(450, 11, 59),
(500, 2, 7),
(500, 6, 6),
(500, 8, 48);

-- --------------------------------------------------------

--
-- Table structure for table `Tag`
--

CREATE TABLE `Tag` (
  `id` int(11) NOT NULL,
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Tag`
--

INSERT INTO `Tag` (`id`, `name`) VALUES
(1, 'chocolate'),
(2, 'vanilla'),
(3, 'asd'),
(4, 'dsa'),
(5, 'fdas'),
(6, 'reqw'),
(7, 'vanilie'),
(8, 'rotund'),
(9, 'prajitura'),
(10, 'cacao'),
(11, 'tiramisu'),
(12, 'frisca'),
(13, 'mere'),
(14, 'napolitana'),
(15, 'nuci'),
(16, 'biscuiti'),
(17, 'ciocolata'),
(18, 'miere'),
(19, 'cafea'),
(20, 'cheesecake'),
(21, 'branza'),
(22, 'negresa'),
(23, 'lamaie'),
(24, 'spanac');

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `username` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gender` int(11) NOT NULL,
  `experience` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `username`, `password`, `date`, `gender`, `experience`) VALUES
(2, 'gicu', 'project3', '2017-12-11 17:04:54', 1, 3),
(1, 'cretz', 'test', '2017-12-11 17:04:56', 1, 2),
(3, 'test123', 'test', '2017-12-11 17:09:17', 2, 2),
(4, 'florik', 'test1', '2017-12-14 13:29:58', 1, 3),
(5, 'security', '12345', '2017-12-14 15:26:34', 1, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Favorite_Tag`
--
ALTER TABLE `Favorite_Tag`
  ADD PRIMARY KEY (`id_user`,`id_tag`);

--
-- Indexes for table `Has_Favorite`
--
ALTER TABLE `Has_Favorite`
  ADD PRIMARY KEY (`id_user`,`id_recipe`);

--
-- Indexes for table `Has_Tag`
--
ALTER TABLE `Has_Tag`
  ADD PRIMARY KEY (`id_recipe`,`id_tag`);

--
-- Indexes for table `Ingredient`
--
ALTER TABLE `Ingredient`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Recipe`
--
ALTER TABLE `Recipe`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Request`
--
ALTER TABLE `Request`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Req_Ingredient`
--
ALTER TABLE `Req_Ingredient`
  ADD PRIMARY KEY (`quantity`,`id_recipe`,`id_ingredient`);

--
-- Indexes for table `Tag`
--
ALTER TABLE `Tag`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Ingredient`
--
ALTER TABLE `Ingredient`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `Recipe`
--
ALTER TABLE `Recipe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `Request`
--
ALTER TABLE `Request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Tag`
--
ALTER TABLE `Tag`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
