create schema smartweb;
use smartweb;

CREATE TABLE `storage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `capacity` int NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `production_order` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `block` (
  `id` int NOT NULL AUTO_INCREMENT,
  `position` int NOT NULL,
  `color` int NOT NULL,
  `storage_id` int NOT NULL,
  `production_order` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Block_Storage` (`storage_id`),
  CONSTRAINT `FK_Block_Storage` FOREIGN KEY (`storage_id`) REFERENCES `storage` (`id`)
);

INSERT INTO `smartweb`.`storage` (`name`, `capacity`) VALUES ('teste', '28');

INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('1', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('2', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('3', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('4', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('5', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('6', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('7', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('8', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('9', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('10', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('11', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('12', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('13', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('14', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('15', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('16', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('17', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('18', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('19', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('20', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('21', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('22', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('23', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('24', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('25', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('26', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('27', '0', '1');
INSERT INTO `smartweb`.`block` (`position`, `color`, `storage_id`) VALUES ('28', '0', '1');
