/*
 Navicat Premium Data Transfer

 Source Server         : localhostMySQL
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : spaceobj-email

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 24/10/2022 22:58:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_email
-- ----------------------------
DROP TABLE IF EXISTS `sys_email`;
CREATE TABLE `sys_email` (
  `email_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '邮箱ID',
  `email_account` varchar(50) DEFAULT NULL COMMENT '邮箱账号',
  `email_password` varchar(50) DEFAULT NULL COMMENT '邮箱密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统邮箱';

-- ----------------------------
-- Records of sys_email
-- ----------------------------
BEGIN;
INSERT INTO `sys_email` (`email_id`, `email_account`, `email_password`, `create_time`, `update_time`) VALUES (1, 'spaceobj@163.com', 'MOQEKYDJGBXPKCCE', '2022-07-27 11:48:13', '2022-09-16 13:55:31');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
