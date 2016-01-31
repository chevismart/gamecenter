/*
Navicat MySQL Data Transfer

Source Server         : wawaonline
Source Server Version : 50621
Source Host           : wawaonline.net:3306
Source Database       : gamecenter

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-02-01 01:02:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `access_channel`
-- ----------------------------
DROP TABLE IF EXISTS `access_channel`;
CREATE TABLE `access_channel` (
  `channelId` int(128) NOT NULL AUTO_INCREMENT,
  `channelName` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`channelId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of access_channel
-- ----------------------------
INSERT INTO `access_channel` VALUES ('1', '微信');

-- ----------------------------
-- Table structure for `alipay`
-- ----------------------------
DROP TABLE IF EXISTS `alipay`;
CREATE TABLE `alipay` (
  `paymentId` varchar(128) NOT NULL,
  `aliPayId` varchar(128) NOT NULL,
  PRIMARY KEY (`aliPayId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of alipay
-- ----------------------------

-- ----------------------------
-- Table structure for `center`
-- ----------------------------
DROP TABLE IF EXISTS `center`;
CREATE TABLE `center` (
  `centerId` int(128) NOT NULL AUTO_INCREMENT,
  `centerName` varchar(64) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `extraParam` varchar(1024) DEFAULT NULL,
  `centerRefId` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`centerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of center
-- ----------------------------
INSERT INTO `center` VALUES ('1', '荔园新天地 ', '1', null, 'liyuanapp');

-- ----------------------------
-- Table structure for `center_channel`
-- ----------------------------
DROP TABLE IF EXISTS `center_channel`;
CREATE TABLE `center_channel` (
  `centerId` int(128) NOT NULL,
  `channelId` int(128) NOT NULL,
  PRIMARY KEY (`centerId`,`channelId`),
  KEY `channelId` (`channelId`),
  CONSTRAINT `center_channel_ibfk_1` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`),
  CONSTRAINT `center_channel_ibfk_2` FOREIGN KEY (`channelId`) REFERENCES `access_channel` (`channelId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of center_channel
-- ----------------------------
INSERT INTO `center_channel` VALUES ('1', '1');

-- ----------------------------
-- Table structure for `center_payment`
-- ----------------------------
DROP TABLE IF EXISTS `center_payment`;
CREATE TABLE `center_payment` (
  `centerId` int(128) NOT NULL,
  `channelId` varchar(128) NOT NULL,
  `paymentId` int(128) NOT NULL,
  `wechatId` varchar(128) NOT NULL,
  PRIMARY KEY (`centerId`,`paymentId`),
  KEY `FK_Relationship_9` (`channelId`,`paymentId`,`wechatId`),
  KEY `paymentId` (`paymentId`),
  CONSTRAINT `center_payment_ibfk_1` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`),
  CONSTRAINT `center_payment_ibfk_2` FOREIGN KEY (`paymentId`) REFERENCES `paymentsystem` (`paymentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of center_payment
-- ----------------------------

-- ----------------------------
-- Table structure for `center_user`
-- ----------------------------
DROP TABLE IF EXISTS `center_user`;
CREATE TABLE `center_user` (
  `centerId` int(128) NOT NULL,
  `userId` int(128) NOT NULL,
  PRIMARY KEY (`centerId`,`userId`),
  KEY `userId` (`userId`),
  CONSTRAINT `center_user_ibfk_1` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`),
  CONSTRAINT `center_user_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of center_user
-- ----------------------------
INSERT INTO `center_user` VALUES ('1', '39');

-- ----------------------------
-- Table structure for `charge_history`
-- ----------------------------
DROP TABLE IF EXISTS `charge_history`;
CREATE TABLE `charge_history` (
  `chargeHistoryId` int(128) NOT NULL AUTO_INCREMENT,
  `customerId` int(128) DEFAULT NULL,
  `wechatId` int(128) DEFAULT NULL,
  `centerId` int(128) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `coin` int(64) DEFAULT NULL,
  `paid` double DEFAULT NULL,
  KEY `cutomerId` (`customerId`),
  KEY `wechatId` (`wechatId`),
  KEY `centerId` (`centerId`),
  KEY `chargeHistoryId` (`chargeHistoryId`) USING BTREE,
  CONSTRAINT `centerId` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cutomerId` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `wechatId` FOREIGN KEY (`wechatId`) REFERENCES `wechat` (`wechatId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of charge_history
-- ----------------------------
INSERT INTO `charge_history` VALUES ('2', '29', '1', null, '2016-02-01 01:02:18', '2', '0');

-- ----------------------------
-- Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `userId` int(128) NOT NULL,
  `customerId` int(128) NOT NULL AUTO_INCREMENT,
  `registerDateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `wallet` float DEFAULT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`customerId`),
  KEY `userId` (`userId`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('39', '29', '2016-01-29 23:28:26', '13', '聪明哥', '1');
INSERT INTO `customer` VALUES ('40', '30', '2015-06-07 01:21:43', '3', 'billy_lee', '1');
INSERT INTO `customer` VALUES ('41', '31', '2015-06-22 21:47:30', '3', '黄绮鸣', '1');
INSERT INTO `customer` VALUES ('42', '32', '2016-01-24 16:37:19', '0', '生果金', '1');
INSERT INTO `customer` VALUES ('43', '33', '2016-01-25 08:59:50', '2', '小鱼子', '1');
INSERT INTO `customer` VALUES ('44', '34', '2016-01-31 17:52:46', '0', '❤YAOFENGYI', '1');
INSERT INTO `customer` VALUES ('45', '35', '2016-01-31 21:39:46', '2', '海怡', '1');
INSERT INTO `customer` VALUES ('46', '36', '2016-01-31 21:47:40', '0', 'Aunk✔', '1');

-- ----------------------------
-- Table structure for `customer_wechat`
-- ----------------------------
DROP TABLE IF EXISTS `customer_wechat`;
CREATE TABLE `customer_wechat` (
  `customerId` int(128) NOT NULL,
  `wechatId` int(128) NOT NULL,
  `openId` varchar(256) NOT NULL,
  `subscribeBonus` bit(4) DEFAULT NULL,
  `subscribeTime` datetime DEFAULT NULL,
  PRIMARY KEY (`customerId`,`wechatId`),
  KEY `wechatId` (`wechatId`),
  CONSTRAINT `customer_wechat_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  CONSTRAINT `customer_wechat_ibfk_2` FOREIGN KEY (`wechatId`) REFERENCES `wechat` (`wechatId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_wechat
-- ----------------------------
INSERT INTO `customer_wechat` VALUES ('29', '1', 'oJpyYuBcMmRKmVCt6AaAKN9EDGac', '', '2015-06-07 00:26:23');
INSERT INTO `customer_wechat` VALUES ('30', '1', 'oJpyYuEVmu6_O4ntXbhyG3GJoUuo', '', '2015-06-07 01:21:43');
INSERT INTO `customer_wechat` VALUES ('31', '1', 'oJpyYuAcuqBiwzCUMQGv7qo3UbkA', '', '2015-06-22 21:47:30');
INSERT INTO `customer_wechat` VALUES ('32', '1', 'oJpyYuG9MkaHqr3yFwZJnS3X8X7k', '', '2016-01-24 16:37:20');
INSERT INTO `customer_wechat` VALUES ('33', '1', 'oJpyYuA0AfkkWC0ek1EthfRWNZ-c', '', '2016-01-25 08:59:50');
INSERT INTO `customer_wechat` VALUES ('34', '1', 'oJpyYuJo5l7dcPmXzQWZ6cnsSii8', '', '2016-01-31 17:52:46');
INSERT INTO `customer_wechat` VALUES ('35', '1', 'oJpyYuFOli37q2oXgSuqoHSUk2SE', '', '2016-01-31 21:39:47');
INSERT INTO `customer_wechat` VALUES ('36', '1', 'oJpyYuFE7E61lxVlxcWaS0OIceSw', '', '2016-01-31 21:47:40');

-- ----------------------------
-- Table structure for `device`
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `deviceId` int(128) NOT NULL AUTO_INCREMENT,
  `centerId` int(128) DEFAULT NULL,
  `deviceName` varchar(64) DEFAULT NULL,
  `deviceDesc` text,
  `macAddr` varchar(64) DEFAULT NULL,
  `powerStatus` varchar(16) DEFAULT NULL,
  `connectionStatus` varchar(16) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`deviceId`),
  KEY `centerId` (`centerId`),
  CONSTRAINT `device_ibfk_1` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('1', '1', 'ATM001', '荔园三楼', 'accf233b95f6', 'OFF', 'OFFLINE', '2016.02.01-01.07.59');
INSERT INTO `device` VALUES ('2', '1', 'ATM002', '新板1', 'accf233b95b6', 'OFF', 'OFFLINE', '2016.02.01-01.07.59');

-- ----------------------------
-- Table structure for `paymentsystem`
-- ----------------------------
DROP TABLE IF EXISTS `paymentsystem`;
CREATE TABLE `paymentsystem` (
  `paymentId` int(128) NOT NULL AUTO_INCREMENT,
  `paymentName` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`paymentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of paymentsystem
-- ----------------------------

-- ----------------------------
-- Table structure for `playrecord`
-- ----------------------------
DROP TABLE IF EXISTS `playrecord`;
CREATE TABLE `playrecord` (
  `refId` varchar(128) DEFAULT NULL,
  `customerId` int(128) NOT NULL,
  `deviceId` int(128) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `playrecordId` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(128) DEFAULT NULL,
  PRIMARY KEY (`playrecordId`),
  KEY `customerId` (`customerId`),
  KEY `deviceId` (`deviceId`),
  CONSTRAINT `playrecord_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  CONSTRAINT `playrecord_ibfk_2` FOREIGN KEY (`deviceId`) REFERENCES `device` (`deviceId`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of playrecord
-- ----------------------------
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-20 09:48:39', '7', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-20 19:12:59', '8', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-23 00:02:56', '9', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-23 09:52:07', '10', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-23 09:52:34', '11', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-23 09:52:34', '12', null);
INSERT INTO `playrecord` VALUES (null, '29', '1', '2016-01-23 10:31:44', '13', null);
INSERT INTO `playrecord` VALUES ('cyAJQeKOAL', '29', '1', '2016-01-24 10:30:26', '14', null);
INSERT INTO `playrecord` VALUES ('AJDsvxMPxG', '29', '1', '2016-01-24 10:30:30', '15', null);
INSERT INTO `playrecord` VALUES ('HRmaWNCLGF', '29', '1', '2016-01-24 12:29:25', '16', null);
INSERT INTO `playrecord` VALUES ('WsoLiJfeLQ', '30', '1', '2016-01-24 12:33:29', '17', null);
INSERT INTO `playrecord` VALUES ('bwXVHzmPyu', '30', '1', '2016-01-24 12:33:45', '18', null);
INSERT INTO `playrecord` VALUES ('saZuSagYuq', '29', '1', '2016-01-24 17:31:24', '19', '1');
INSERT INTO `playrecord` VALUES ('MqSwGJBBuT', '29', '1', '2016-01-24 17:31:49', '20', '3');
INSERT INTO `playrecord` VALUES ('vociCiijIW', '30', '1', '2016-01-26 21:12:31', '21', '5');
INSERT INTO `playrecord` VALUES ('BoEWQfnNfl', '29', '1', '2016-01-26 21:45:21', '22', '5');
INSERT INTO `playrecord` VALUES ('NDTRNjKVRY', '29', '1', '2016-01-27 19:35:11', '23', '5');
INSERT INTO `playrecord` VALUES ('tsLSceYAon', '29', '1', '2016-01-29 21:40:53', '24', '1');
INSERT INTO `playrecord` VALUES ('mpqrOffjkF', '29', '1', '2016-01-29 21:51:36', '25', '1');
INSERT INTO `playrecord` VALUES ('XPVoUaEtPv', '29', '1', '2016-01-29 21:53:37', '26', '1');
INSERT INTO `playrecord` VALUES ('AlrPvupjeA', '29', '1', '2016-01-29 21:53:42', '27', '1');
INSERT INTO `playrecord` VALUES ('JuZiZqEgKm', '29', '1', '2016-01-29 21:54:39', '28', '1');
INSERT INTO `playrecord` VALUES ('SzBFCSomqu', '29', '1', '2016-01-29 21:57:16', '29', '1');
INSERT INTO `playrecord` VALUES ('cMsPFwYRhV', '29', '1', '2016-01-29 22:03:33', '30', '1');
INSERT INTO `playrecord` VALUES ('hTkVBHdYdH', '29', '1', '2016-01-29 22:04:01', '31', '1');
INSERT INTO `playrecord` VALUES ('omzrMyUabz', '29', '1', '2016-01-31 12:03:53', '32', '1');
INSERT INTO `playrecord` VALUES ('sCdCNMpdon', '29', '1', '2016-01-31 12:03:59', '33', '100');
INSERT INTO `playrecord` VALUES ('cCOmuFRdXi', '29', '1', '2016-01-31 12:04:02', '34', '100');
INSERT INTO `playrecord` VALUES ('ZUNpiQHGoQ', '29', '1', '2016-01-31 12:06:23', '35', '1');
INSERT INTO `playrecord` VALUES ('NaggUYnJja', '29', '1', '2016-01-31 12:06:47', '36', '3');
INSERT INTO `playrecord` VALUES ('UyjUfwuTQU', '34', '1', '2016-01-31 17:53:37', '37', '1');
INSERT INTO `playrecord` VALUES ('rEbYuBIqld', '34', '1', '2016-01-31 17:53:53', '38', '1');
INSERT INTO `playrecord` VALUES ('cDOaMpomCl', '36', '1', '2016-01-31 21:48:23', '39', '3');

-- ----------------------------
-- Table structure for `staff`
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `userId` int(128) NOT NULL,
  `staffId` int(128) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`staffId`),
  KEY `userId` (`userId`),
  CONSTRAINT `staff_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES ('39', '1', '经理', '1');
INSERT INTO `staff` VALUES ('39', '2', '操作员', '1');

-- ----------------------------
-- Table structure for `trade`
-- ----------------------------
DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
  `tradeId` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(128) DEFAULT NULL,
  `centerId` int(128) DEFAULT NULL,
  `tradeTime` datetime DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `paymentSystem` varchar(64) DEFAULT NULL,
  `isPaid` int(11) DEFAULT NULL,
  PRIMARY KEY (`tradeId`),
  KEY `customerId` (`customerId`),
  KEY `centerId` (`centerId`),
  CONSTRAINT `trade_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  CONSTRAINT `trade_ibfk_2` FOREIGN KEY (`centerId`) REFERENCES `center` (`centerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trade
-- ----------------------------
INSERT INTO `trade` VALUES ('1', '29', '1', '2015-08-30 19:48:55', '1', '121', '0');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` int(128) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('39', '聪明哥', '1');
INSERT INTO `user` VALUES ('40', 'billy_lee', '1');
INSERT INTO `user` VALUES ('41', '黄绮鸣', '1');
INSERT INTO `user` VALUES ('42', '生果金', '1');
INSERT INTO `user` VALUES ('43', '小鱼子', '1');
INSERT INTO `user` VALUES ('44', '❤YAOFENGYI', '1');
INSERT INTO `user` VALUES ('45', '海怡', '1');
INSERT INTO `user` VALUES ('46', 'Aunk✔', '1');

-- ----------------------------
-- Table structure for `user_channel`
-- ----------------------------
DROP TABLE IF EXISTS `user_channel`;
CREATE TABLE `user_channel` (
  `userId` varchar(128) NOT NULL,
  `channelId` varchar(128) NOT NULL,
  PRIMARY KEY (`userId`,`channelId`),
  KEY `FK_user_channel2` (`channelId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_channel
-- ----------------------------

-- ----------------------------
-- Table structure for `wechat`
-- ----------------------------
DROP TABLE IF EXISTS `wechat`;
CREATE TABLE `wechat` (
  `wechatId` int(128) NOT NULL AUTO_INCREMENT,
  `channelName` varchar(128) DEFAULT NULL,
  `wechatAppName` varchar(512) DEFAULT NULL,
  `wechatAppId` varchar(128) DEFAULT NULL,
  `wechatAppSecret` varchar(128) DEFAULT NULL,
  `wechatMchId` varchar(128) DEFAULT NULL,
  `wechatPayKey` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`wechatId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat
-- ----------------------------
INSERT INTO `wechat` VALUES ('1', null, '扬丰百货', 'wxe89a9d2fa17df80f', '71d8fc7778571e6b54712953b68084e4', '1224905202', null);
