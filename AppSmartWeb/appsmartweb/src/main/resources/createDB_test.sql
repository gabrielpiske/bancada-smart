CREATE SCHEMA IF NOT EXISTS smartweb;
USE smartweb;

CREATE TABLE IF NOT EXISTS `storage` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `capacity` int NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `production_order` bigint NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `block` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position` int NOT NULL,
  `color` int NOT NULL,
  `storage_id` bigint NOT NULL,
  `production_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Block_Storage` (`storage_id`),
  KEY `FK_Block_Orders` (`production_order_id`),
  CONSTRAINT `FK_Block_Storage` FOREIGN KEY (`storage_id`) REFERENCES `storage` (`id`),
  CONSTRAINT `FK_Block_Orders` FOREIGN KEY (`production_order_id`) REFERENCES `orders` (`id`)
);

INSERT INTO `smartweb`.`storage` (`id`, `name`, `capacity`) VALUES (1, 'Stock', 28)
  ON DUPLICATE KEY UPDATE `name` = 'Stock', `capacity` = 28;
INSERT INTO `smartweb`.`storage` (`id`, `name`, `capacity`) VALUES (2, 'Expedition', 12)
  ON DUPLICATE KEY UPDATE `name` = 'Expedition', `capacity` = 12;

INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (1, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (2, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (3, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (4, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (5, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (6, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (7, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (8, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (9, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (10, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (11, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (12, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (13, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (14, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (15, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (16, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (17, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (18, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (19, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (20, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (21, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (22, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (23, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (24, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (25, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (26, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (27, 0, 1);
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES (28, 0, 1);
