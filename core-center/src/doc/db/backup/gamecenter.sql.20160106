/*
Navicat MySQL Data Transfer

Source Server         : wawaonline
Source Server Version : 50621
Source Host           : wawaonline.net:3306
Source Database       : gamecenter

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-01-06 22:24:28
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
  PRIMARY KEY (`centerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of center
-- ----------------------------
INSERT INTO `center` VALUES ('1', '荔园新天地 ', '1', null);

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

-- ----------------------------
-- Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `userId` int(128) NOT NULL,
  `customerId` int(128) NOT NULL AUTO_INCREMENT,
  `registerDateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `wallet` float DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`customerId`),
  KEY `userId` (`userId`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('39', '29', '2015-08-24 23:22:39', '1', '聪明哥', '1');
INSERT INTO `customer` VALUES ('40', '30', '2015-06-07 01:21:43', '0', 'billy_lee', '1');
INSERT INTO `customer` VALUES ('41', '31', '2015-06-22 21:47:30', '0', '黄绮鸣', '1');

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
INSERT INTO `customer_wechat` VALUES ('31', '1', 'oJpyYuAcuqBiwzCUMQGv7qo3UbkA', '', '2015-06-22 21:47:30');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('1', '1', 'testDeviceName', null, 'ATM002', null, null, null);

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
  `customerId` int(128) NOT NULL,
  `deviceId` int(128) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `playrecordId` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`playrecordId`),
  KEY `customerId` (`customerId`),
  KEY `deviceId` (`deviceId`),
  CONSTRAINT `playrecord_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  CONSTRAINT `playrecord_ibfk_2` FOREIGN KEY (`deviceId`) REFERENCES `device` (`deviceId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of playrecord
-- ----------------------------
INSERT INTO `playrecord` VALUES ('29', '1', '2015-06-22 14:48:56', '1');
INSERT INTO `playrecord` VALUES ('29', '1', '2015-06-22 15:18:02', '2');
INSERT INTO `playrecord` VALUES ('29', '1', '2015-06-22 15:23:51', '3');
INSERT INTO `playrecord` VALUES ('30', '1', '2015-06-22 21:47:28', '4');
INSERT INTO `playrecord` VALUES ('29', '1', '2015-08-30 19:01:57', '5');
INSERT INTO `playrecord` VALUES ('30', '1', '2015-08-30 21:09:48', '6');

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
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('39', '聪明哥', '1');
INSERT INTO `user` VALUES ('40', 'billy_lee', '1');
INSERT INTO `user` VALUES ('41', '黄绮鸣', '1');

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
