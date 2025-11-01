-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: travel_db
-- ------------------------------------------------------
-- Server version	8.0.43

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `package_id` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `no_of_persons` int DEFAULT '1',
  `hotel_preference` varchar(100) DEFAULT NULL,
  `status` enum('Pending','Confirmed','Cancelled') DEFAULT 'Pending',
  `booking_date` date DEFAULT (curdate()),
  `guests` int NOT NULL DEFAULT '1',
  `hotel_type` varchar(50) DEFAULT NULL,
  `duration_days` int DEFAULT NULL,
  `checkin` date DEFAULT NULL,
  `checkout` date DEFAULT NULL,
  `adults` int DEFAULT '1',
  `children` int DEFAULT '0',
  `payment_status` varchar(20) DEFAULT 'Unpaid',
  PRIMARY KEY (`id`),
  KEY `package_id` (`package_id`),
  KEY `bookings_ibfk_1` (`user_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,2,1,'2025-12-10','2025-12-13',2,'Resort','Confirmed','2025-10-10',1,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(2,4,4,NULL,NULL,NULL,NULL,'Confirmed','2025-10-10',3,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(3,4,5,NULL,NULL,NULL,NULL,'Confirmed','2025-10-10',5,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(4,4,5,'2025-12-12','2025-12-19',5,'5 star','Pending','2025-10-10',5,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(5,4,3,NULL,NULL,NULL,NULL,'Pending','2025-10-10',1,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(6,5,1,NULL,NULL,NULL,NULL,'Pending','2025-10-10',1,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(7,4,1,NULL,NULL,NULL,NULL,'Pending','2025-10-10',1,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(8,5,1,NULL,NULL,NULL,NULL,'Pending','2025-10-10',1,NULL,NULL,NULL,NULL,1,0,'Unpaid'),(9,7,1,'2025-10-30','2025-10-31',1,'5 Star','Confirmed','2025-10-30',1,NULL,NULL,NULL,NULL,1,0,'Paid'),(10,7,1,'2025-10-31','2025-10-31',1,'Any','Pending','2025-10-31',1,NULL,NULL,NULL,NULL,1,0,'Unpaid');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extras`
--

DROP TABLE IF EXISTS `extras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extras` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extras`
--

LOCK TABLES `extras` WRITE;
/*!40000 ALTER TABLE `extras` DISABLE KEYS */;
/*!40000 ALTER TABLE `extras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `description` text,
  `budget` double DEFAULT NULL,
  `duration_days` int DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT '0',
  `review_count` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `destination_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
INSERT INTO `packages` VALUES (1,'Goa Vacation','Goa, India','Beach','Sun, sand and sea.',15000,3,NULL,4.5,20,'2025-10-09 23:49:50',NULL),(2,'Manali Trek','Manali, India','Mountain','Himalayan trek and views.',20000,4,NULL,4.7,15,'2025-10-09 23:49:50',NULL),(3,'Paris Romantic','Paris, France','City','Eiffel Tower, romance & lights.',60000,5,NULL,4,1,'2025-10-09 23:49:50',NULL),(4,'Bheem','Dholakpur','Village','Bheem hi shakti dhoom machaye!',14123,12,NULL,0,0,'2025-10-10 03:13:07',NULL),(5,'Shinchan','Kasukabe','City','Mera naam hi sinchan hai!',12313,12,NULL,0,0,'2025-10-10 04:11:24',NULL),(6,'Ash','Palet Town','Town','Banunga me itna jabardast',1233131,12313,NULL,0,0,'2025-10-10 05:33:38',NULL);
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int DEFAULT NULL,
  `payment_mode` enum('Card','UPI','NetBanking') DEFAULT NULL,
  `amount_paid` double DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,'UPI',30000,'TXN123456789','2025-10-09 23:50:19');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `package_id` int NOT NULL,
  `rating` int NOT NULL,
  `review` text,
  `review_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `package_id` (`package_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (2,4,1,1,'its a very beautiful place to visit, my experience was very gr8.','2025-10-09 18:30:00'),(3,5,1,1,'its a very nice place to visit','2025-10-09 18:30:00'),(4,7,1,1,'nice to visit\n','2025-10-29 18:30:00');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `contact` varchar(15) DEFAULT NULL,
  `role` enum('admin','customer') DEFAULT 'customer',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin1','admin123','Admin One','admin@example.com','9999000000','admin'),(2,'john','john123','John Doe','john@example.com','8888111122','customer'),(4,'devesh','abcd123','devesh n','abc@123','123321123','customer'),(5,'aditya','skkalm',NULL,NULL,NULL,'customer'),(6,'admin2','simple123',NULL,NULL,NULL,'customer'),(7,'user1','user123','Devesh','deveshnaik1003@gmail.com','8591111233','customer');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `package_id` int DEFAULT NULL,
  `added_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `package_id` (`package_id`),
  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES (1,4,4,'2025-10-10 03:30:09'),(2,4,5,'2025-10-10 04:12:56'),(3,4,5,'2025-10-10 05:11:06');
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


-- Dump completed on 2025-10-31  1:25:51

-- Travel Recommendation System Database
-- SQL Script to create and populate the database
CREATE DATABASE IF NOT EXISTS travel_db;
USE travel_db;

-- ==========================
-- USERS TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('admin', 'customer') DEFAULT 'customer'
);

-- ==========================
-- PACKAGES TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS packages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    type VARCHAR(50),
    budget DOUBLE,
    rating DOUBLE DEFAULT 0
);

-- ==========================
-- BOOKINGS TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    no_of_persons INT DEFAULT 1,
    hotel_preference VARCHAR(100),
    status ENUM('Pending','Confirmed','Cancelled','Paid') DEFAULT 'Pending',
    alert_status VARCHAR(100) DEFAULT 'No Alert', -- 👈 Added alert column
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(package_id) REFERENCES packages(id) ON DELETE CASCADE
);

-- ==========================
-- REVIEWS TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(package_id) REFERENCES packages(id) ON DELETE CASCADE
);

-- ==========================
-- PAYMENTS TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('Pending','Success','Failed') DEFAULT 'Pending', -- 👈 Added status
    FOREIGN KEY(booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

-- ==========================
-- WISHLIST TABLE
-- ==========================
CREATE TABLE IF NOT EXISTS wishlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(package_id) REFERENCES packages(id) ON DELETE CASCADE
);

-- ==========================
-- SAMPLE DATA
-- ==========================
INSERT INTO users(username, password, role) VALUES
('admin', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users(username, password, role) VALUES
('user1', 'user123', 'customer')
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO packages(name, location, type, budget, rating) VALUES
('Goa Beach Trip', 'Goa', 'Leisure', 15000, 4.5),
('Himalaya Trek', 'Himachal', 'Adventure', 25000, 4.8),
('Kerala Backwaters', 'Kerala', 'Leisure', 20000, 4.6)
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO bookings(user_id, package_id, no_of_persons, hotel_preference, status, alert_status) VALUES
(2, 1, 2, 'Sea View Hotel', 'Pending', 'No Alert'),
(2, 2, 1, 'Mountain Lodge', 'Confirmed', 'Ready for Payment')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO reviews(user_id, package_id, rating, review) VALUES
(2, 1, 5, 'Amazing experience at Goa!'),
(2, 2, 4, 'Challenging trek but worth it.')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO payments(booking_id, amount, payment_status) VALUES
(1, 30000, 'Pending'),
(2, 25000, 'Success')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO wishlist(user_id, package_id) VALUES
(2, 3)
ON DUPLICATE KEY UPDATE id=id;

