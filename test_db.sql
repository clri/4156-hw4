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
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `jobdesc` varchar(255) DEFAULT NULL,
  `jobtitle` varchar(255) DEFAULT NULL,
  `payrate` double DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,NULL,'sdfgh','asdfgh',NULL,1),(2,NULL,'dsafsd','dsfkjlfd',NULL,1),(3,NULL,'','blah blah blah',NULL,2);
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee` int(11) DEFAULT NULL,
  `employer` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `review_body` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,NULL,'email@email.com','your first name',NULL,NULL,'$2a$10$ViScD8nxBLC5M8uyRsWmS.qUd6D0jtfdTQoSCqvdVUAs16kixmkRm','2017-10-26 09:12:22',NULL),(2,NULL,'email@email.comm','your first name',NULL,NULL,'$2a$10$3YEGvxklRbQnFQazuDiHZODaQadS11N84iRa3HYxzI4Cr5irx7i0K','2017-10-29 18:15:45',NULL),(6,NULL,'email@email.como','your first name',NULL,NULL,'$2a$10$njq25Eurjp0F64OI0VF72.AmoKEfFDISbXL35KHv4RITx.wLCoFyK','2017-11-01 18:05:46',NULL),(7,NULL,'email@email.comrrr','your first name',NULL,NULL,'$2a$10$UHweDjPeLB4zbAw3sHBFQ.0CZxvM652lSFRp5niqEPI4JW5iCmYBe','2017-11-01 18:13:50',NULL),(8,NULL,'abc@columbia.edu','your first name',NULL,NULL,'$2a$10$t7SEZugwr7yj95sD4B.6iOIBom6WiMib4CrgvETTtUgE.Ca418VfK','2017-11-01 18:24:42',NULL),(9,NULL,'abc@barnard.edu','your first name',NULL,NULL,'$2a$10$FHqcJ2fhWPhgH0H6sHHQuuPNdHUp8bJfILHbcbgv2E8k.WkA4D6Uy','2017-11-01 18:24:51',NULL),(10,'BS, MA, PhD, etc','bbj@barnard.edu','billy bob','johnson','ON or OFF (campus)','$2a$10$4TodZeqBcniVKIIgFTxteunwfcYVjRfXcasHCzNitqo11zNw7HWMm','2017-11-01 19:16:30','SEAS, Barnard, Teachers\' College...'),(11,'BS, MA, PhD, etc','bbj@columbia.edu','9837493284','johnson','ON or OFF (campus)','$2a$10$FkiDKA3AY5yDBpgCipWTb.dqYiL15gqoc1zRs4a1Nz6U2eAfiNFQi','2017-11-01 19:16:52','SEAS, Barnard, Teachers\' College...'),(12,'BS, MA, PhD, etc','mjs@barnard.edu','mary-jo','stevens','ON or OFF (campus)','$2a$10$kxUyigWa8mA8n0e9zJG3c.dUHb2CA87s4pFN0rX5WKrM9IOBZoB/W','2017-11-01 19:35:34','SEAS, Barnard, Teachers\' College...'),(13,'BS, MA, PhD, etc','mdjs@barnard.edu','mary jo','stevens','ON or OFF (campus)','$2a$10$VVVgmqTeEUxymr/uW9tlq.yYhF2xyPWcCzCU1k0mUMLUNbIJLOy4C','2017-11-01 19:35:46','SEAS, Barnard, Teachers\' College...');
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

-- Dump completed on 2017-11-10 15:26:55
