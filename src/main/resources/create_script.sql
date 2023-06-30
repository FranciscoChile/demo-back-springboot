CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation_date` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `valid` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) 