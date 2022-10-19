-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: questionnairesystem
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `fbid` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_phone` varchar(10) NOT NULL,
  `user_email` varchar(45) NOT NULL,
  `user_age` int NOT NULL,
  `createtime` datetime NOT NULL,
  `answer` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `postid` varchar(45) NOT NULL,
  PRIMARY KEY (`fbid`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (16,'123','0912345678','1@1',20,'2022-09-13 15:44:26','[{\"key\":\"74\",\"value\":\"Kobe\"},{\"key\":\"75\",\"value\":\"Kobe,KD,LBJ,Curry,PG,KW,KG,\"}]','cd1a4b2b-d202-43d8-b193-107ea84a4f9e'),(17,'Test 9/14','0912345678','1@1',20,'2022-09-14 12:58:21','[{\"key\":\"74\",\"value\":\"Kobe\"}]','cd1a4b2b-d202-43d8-b193-107ea84a4f9e'),(18,'Test 9/14','0912345678','1@1',23,'2022-09-14 14:48:49','[{\"key\":\"75\",\"value\":\"Kobe,KD,\"}]','cd1a4b2b-d202-43d8-b193-107ea84a4f9e'),(30,'Test 10/6','0912345678','30@2323',14,'2022-10-06 13:20:13','[{\"key\":\"137\",\"value\":\"台中\"}]','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(31,'Test 10/6','0912345678','1@1',16,'2022-10-06 13:20:58','[{\"key\":\"135\",\"value\":\"456,\"},{\"key\":\"137\",\"value\":\"台南\"}]','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(32,'123','0912345678','1@1',4,'2022-10-06 13:21:21','[{\"key\":\"135\",\"value\":\"123,\"},{\"key\":\"137\",\"value\":\"台北\"}]','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(33,'123','0912345678','1@1',5,'2022-10-06 13:22:09','[{\"key\":\"135\",\"value\":\"123,\"},{\"key\":\"137\",\"value\":\"台中\"}]','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(34,'Test 9/14','0912345678','30@2323',30,'2022-10-06 16:59:57','[{\"key\":\"140\",\"value\":\"湖人\"},{\"key\":\"141\",\"value\":\"湖人,\"}]','0f030380-9a9a-4966-9024-ec45da56021e'),(35,'Test 9/14','0923456789','1@1',10,'2022-10-06 17:00:16','[{\"key\":\"140\",\"value\":\"勇士\"},{\"key\":\"141\",\"value\":\"勇士,\"}]','0f030380-9a9a-4966-9024-ec45da56021e'),(36,'123','0923456789','30@2323',4,'2022-10-06 17:00:50','[{\"key\":\"140\",\"value\":\"國王\"},{\"key\":\"141\",\"value\":\"快艇,\"}]','0f030380-9a9a-4966-9024-ec45da56021e'),(37,'賴承厚','0912345678','1@1',17,'2022-10-15 20:58:47','[{\"key\":\"140\",\"value\":\"湖人\"},{\"key\":\"141\",\"value\":\"\"},{\"key\":\"142\",\"value\":\"123\"}]','0f030380-9a9a-4966-9024-ec45da56021e');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frenquenquestion`
--

DROP TABLE IF EXISTS `frenquenquestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frenquenquestion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `caption` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `selection` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int NOT NULL,
  `nullable` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frenquenquestion`
--

LOCK TABLES `frenquenquestion` WRITE;
/*!40000 ALTER TABLE `frenquenquestion` DISABLE KEYS */;
INSERT INTO `frenquenquestion` VALUES (11,'1231321','123',1,1),(13,'8/30 TEST','123;123;123',1,0);
/*!40000 ALTER TABLE `frenquenquestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personinfo`
--

DROP TABLE IF EXISTS `personinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personinfo` (
  `personid` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `email` varchar(45) NOT NULL,
  `account` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`personid`),
  UNIQUE KEY `personid_UNIQUE` (`personid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personinfo`
--

LOCK TABLES `personinfo` WRITE;
/*!40000 ALTER TABLE `personinfo` DISABLE KEYS */;
INSERT INTO `personinfo` VALUES ('9b1a7f22-a772-4c72-8739-baed966c715e','02','1234567802','02@gmail.com','admin','12345');
/*!40000 ALTER TABLE `personinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `quid` int NOT NULL AUTO_INCREMENT,
  `caption` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int NOT NULL,
  `nullable` varchar(45) NOT NULL,
  `selection` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `postid` varchar(45) NOT NULL,
  PRIMARY KEY (`quid`),
  UNIQUE KEY `quid_UNIQUE` (`quid`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (74,'NBA 球員',0,'off','Kobe;KD;LBJ;Curry;PG;KW;KG','cd1a4b2b-d202-43d8-b193-107ea84a4f9e'),(75,'NBA 球員',1,'on','Kobe;KD;LBJ;Curry;PG;KW;KG','cd1a4b2b-d202-43d8-b193-107ea84a4f9e'),(125,'1231321',1,'off','123','22c88d97-1193-4fc5-b214-da459f05011b'),(126,'1231321',1,'off','123','22c88d97-1193-4fc5-b214-da459f05011b'),(127,'1231321',1,'off','123','22c88d97-1193-4fc5-b214-da459f05011b'),(128,'1231321',1,'on','123','22c88d97-1193-4fc5-b214-da459f05011b'),(135,'9/28 TEST',1,'off','123;456;789','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(136,'測試問卷 9/28 ',2,'off','測試問卷 9/28 ','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(137,'居住地',0,'on','台北;台中;台南;高雄','bba70bcf-5dc6-49ab-90eb-1b28413a0368'),(140,'最喜歡 NBA 球隊',0,'on','湖人;勇士;太陽;快艇;國王','0f030380-9a9a-4966-9024-ec45da56021e'),(141,'最喜歡 NBA 球隊',1,'off','湖人;勇士;太陽;快艇;國王','0f030380-9a9a-4966-9024-ec45da56021e'),(142,'8/30 TEST',0,'on','123;456;789','0f030380-9a9a-4966-9024-ec45da56021e');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey`
--

DROP TABLE IF EXISTS `survey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey` (
  `postid` varchar(45) NOT NULL,
  `title` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `body` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `available` int NOT NULL,
  PRIMARY KEY (`postid`),
  UNIQUE KEY `postid_UNIQUE` (`postid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
INSERT INTO `survey` VALUES ('0f030380-9a9a-4966-9024-ec45da56021e','測試問卷10/6','測試問卷10/6','2022-10-06 09:00:00','2022-10-20 09:00:00','2022-10-06 16:58:13',1),('22c88d97-1193-4fc5-b214-da459f05011b','測試問卷 9/16 VOL1','測試問卷 9/16 VOL1','2022-09-21 09:00:00','2022-10-10 09:00:00','2022-09-21 10:46:51',0),('bba70bcf-5dc6-49ab-90eb-1b28413a0368','測試問卷 9/28 ','測試問卷 9/28 ','2022-09-28 09:00:00','2022-10-10 09:00:00','2022-09-28 12:02:42',0),('cd1a4b2b-d202-43d8-b193-107ea84a4f9e','123','123','2022-09-20 09:00:00','2022-10-12 09:00:00','2022-09-13 15:42:55',0);
/*!40000 ALTER TABLE `survey` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-10-19 16:16:26
