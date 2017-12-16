-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: localhost    Database: db_example
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Reset_Token`
--

DROP TABLE IF EXISTS `Reset_Token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Reset_Token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_email` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reset_Token`
--

LOCK TABLES `Reset_Token` WRITE;
/*!40000 ALTER TABLE `Reset_Token` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reset_Token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `jobdesc` varchar(1500) DEFAULT NULL,
  `jobtitle` varchar(255) DEFAULT NULL,
  `payrate` double DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (12,'pets','I have a chihuahua (female)','Walk my dog!',NULL,17),(13,'plants','Will be out of town from the 25th to the 28th of December. I have a cactus and several lemon trees.','Water my plants',NULL,17),(14,'video games','I need someone to level up my Battlefront II character. Experienced gamer desired','Video Gamer Needed',NULL,17),(15,'tutoring','My 5 year old daughter needs a tutor for the SATs. Please contact me with proof of good SAT scores.','Tutor',NULL,17),(16,'errands','I need someone to buy me a tall latte every morning from starbucks. Extra hot. Will reimburse you with drink of your choice.','gofer needed',NULL,23),(17,'pets','Feed my maine coon cat. Precise measurements of food needed.','Feed my cat',NULL,23),(18,'pets','My dog Fido has a vet appointment he really needs and I have an important meeting that can\'t be rescheduled. Contact ASAP!','Take Fido to the vet',NULL,23),(19,'tutoring','Need a tutor for advanced software engineering. Must know JUnit!','Tutor',NULL,19),(20,'pets walk outside','Walk my two dogs. Both neutered, old, good with people.','Dog walking',NULL,19),(21,'plants','I have 2 ferns but I forget to water them all the time so please text me every morning with angry emoji','remind me to water my plants',NULL,19),(22,'survey','Hi, I need someone to take a very long survey for a class I\'m doing. Must be willing to take this seriously.','Take survey',NULL,16),(23,'errands','Need a party catered. Must send resume when contacting me.','Catering',NULL,16),(24,'chores','easy. no stains, no special items (but please don\'t put my jeans in the dryer)','do my laundry',NULL,16),(27,'pets','Feed my fish two times a day for one week in January.','Feed my fish',NULL,27);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee` int(11) DEFAULT NULL,
  `employer` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `employer_read` tinyint(1) DEFAULT NULL,
  `employee_read` tinyint(1) DEFAULT NULL,
  `request_time` datetime DEFAULT NULL,
  `decision_time` datetime DEFAULT NULL,
  `decision` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
INSERT INTO `request` VALUES (4,19,23,16,'','gofer needed',0,NULL,'2017-12-12 13:27:36',NULL,0),(5,16,23,17,'','Feed my cat',0,NULL,'2017-12-12 13:33:58',NULL,0),(6,22,16,23,'','Catering',1,NULL,'2017-12-12 13:37:27','2017-12-12 14:02:11',2),(7,27,16,23,'','Catering',1,1,'2017-12-12 14:01:16','2017-12-12 14:02:05',1);
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 trigger req_update before update on request
for each ROW
BEGIN
        declare noe int;
        if new.decision = 1 and old.decision != 1 then
                select count(*) into noe from request r where r.job = new.job and r.decision = 1;
                if (noe is not null and noe > 0) then
                        signal sqlstate '12345';
                end if;
        end if;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee` int(11) DEFAULT NULL,
  `employer` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `review_body` varchar(1500) DEFAULT NULL,
  `author` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (7,27,16,23,3.5,'Easy, paid on time.',27);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_degree` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_first_name` varchar(255) DEFAULT NULL,
  `user_last_name` varchar(255) DEFAULT NULL,
  `user_location` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_reg_date` datetime DEFAULT NULL,
  `user_school` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `user_email_2` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (16,'BS','clr2176@columbia.edu','Elvis','Presley','OFF','$2a$10$OKoVW1xgDj3fkdrqoNeR.Of5LATfz/2kbh2NGRF3HO3dQFKYMZc8G','2017-12-12 12:49:46','SEAS'),(17,'MA','bbj@columbia.edu','Billy Bob','Jones','ON','$2a$10$Vaf2zhaDkBtC8ZktJ3bm0.3uUKtguyzP/uyXaqi4i1ETWHIZ3I6A.','2017-12-12 13:15:41','SEAS'),(18,'BA','rrr7@barnard.edu','Rhonda','Roberston','OFF','$2a$10$xLS0yBTUPsoQ.tsIZEP4lOJ1LW0wBm2tr6OKXZnYUkZTbN3dpzFzi','2017-12-12 13:16:17','Barnard'),(19,'PhD','kfc@cs.columbia.edu','Kate','Chan','ON','$2a$10$BxrUXq/VynFw4gK5QXyf/eb8zwVpL5.H7Rr4uDNwMuWcE/o7sHl3S','2017-12-12 13:17:13','SEAS'),(20,'MA','rs6666@columbia.edu','Rayner','Sanches','ON','$2a$10$wJpY.rxsIO3DeW1QQLz8M.gn4B1o7Dyls5efK5KCu.lbeDjPp3/hK','2017-12-12 13:17:38','TC'),(21,'BS','jeb@columbia.edu','Jane','Blah','ON','$2a$10$e5PoI7axc0ZtcPML0C828eVih/cAY/yg2hBPomf.Y5pq4WGTaVY8i','2017-12-12 13:17:58','SEAS'),(22,'MA','rrr78@columbia.edu','Roger','Roy','ON','$2a$10$T5/7/X/q9j6T9jxG.vc4iecMIhf3w897Ethxl/ExmUKW88IaKZjtO','2017-12-12 13:18:29','SEAS'),(23,'MA','jfg567@columbia.edu','Jillian','Gupta','ON','$2a$10$SPd8Km2gl6QRJLhBFtS2UOrEeRo94Vk4kBbzhjYvGA1aELB5opuea','2017-12-12 13:19:01','SEAS'),(24,'BS','cmp2223@columbia.edu','Christine','Pape','ON','$2a$10$R/X247EQ1Jf0gibpebA/j.2m.8vvRnjXeiynE2eHBea8q2i4rEsaq','2017-12-12 13:53:50','SEAS'),(25,'BS','rk2846@barnard.edu','Riya','Kumar','ON','$2a$10$CmR16nMll8xacDAtjEojOeZA6Cojbf1D0cav7r5tdG0UAsgYwCYPC','2017-12-12 13:54:18','Barnard'),(26,'MS','jeongmin.oh@columbia.edu','Jeongmin','Oh','OFF','$2a$10$gFNShotrMbpgBEaGPmm5O.yb/9zFOJIN.HsIn6nrJzFo6lSVM2cZ2','2017-12-12 13:54:52','SEAS'),(27,'BS','email@barnard.edu','Blah','Blah','ON','$2a$10$uV5Gn26dVuq8TmTOAvTNRuirLViK6fOFrnJ5GA6dEb2iU.Qebf1Wu','2017-12-12 13:59:52','SEAS');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!50001 DROP VIEW IF EXISTS `user_roles`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `user_roles` AS SELECT 
 1 AS `user_role_id`,
 1 AS `username`,
 1 AS `role`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `user_roles`
--

/*!50001 DROP VIEW IF EXISTS `user_roles`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_roles` AS select `user`.`id` AS `user_role_id`,`user`.`id` AS `username`,'ROLE_USER' AS `role` from `user` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-12 15:12:39
