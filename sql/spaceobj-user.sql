/*
 Navicat Premium Data Transfer

 Source Server         : myserver002
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 114.132.226.147:3306
 Source Schema         : spaceobj-user

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 05/11/2022 21:41:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jd_advertise
-- ----------------------------
DROP TABLE IF EXISTS `jd_advertise`;
CREATE TABLE `jd_advertise` (
  `jd_ad_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '广告ID',
  `jd_ad_hyperlink` varchar(200) DEFAULT NULL COMMENT '广告超链接',
  `jd_ad_image_link` varchar(200) DEFAULT NULL COMMENT '图片链接',
  `jd_ad_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `jd_ad_store_name` varchar(50) DEFAULT NULL COMMENT '商店名称',
  `jd_ad_coupon` varchar(50) DEFAULT NULL COMMENT '优惠券描述',
  `jd_ad_price` decimal(50,2) DEFAULT NULL COMMENT '价格描述',
  `jd_ad_comment_num` varchar(50) DEFAULT NULL COMMENT '好评数量',
  `jd_ad_create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jd_ad_update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`jd_ad_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121180162 DEFAULT CHARSET=utf8 COMMENT='京东广告';

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
-- Table structure for sys_photo
-- ----------------------------
DROP TABLE IF EXISTS `sys_photo`;
CREATE TABLE `sys_photo` (
  `photo_id` varchar(32) NOT NULL COMMENT '主键ID',
  `photo_url` text COMMENT '图片链接',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户头像表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `invite_user_id` varchar(50) DEFAULT NULL COMMENT '邀请人id',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `email_code` varchar(50) DEFAULT NULL COMMENT '邮箱验证码',
  `password` text COMMENT '密码',
  `token` varchar(50) DEFAULT NULL COMMENT '登录的token',
  `open_id` varchar(200) DEFAULT NULL COMMENT 'open_id',
  `phone_number` varchar(50) DEFAULT NULL COMMENT '电话号码',
  `assist_value` int(11) DEFAULT '0' COMMENT '助力值',
  `invitation_value` int(11) DEFAULT '0' COMMENT '邀请值',
  `user_type` varchar(200) DEFAULT NULL COMMENT '用户类型',
  `user_rights` varchar(200) DEFAULT NULL COMMENT '用户权限',
  `username` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `photo_url` text COMMENT '头像',
  `online_status` int(11) DEFAULT '0' COMMENT '在线状态：0表示下线，1表示在线',
  `real_name_status` int(11) DEFAULT '0' COMMENT '实名状态0表示未实名，1表示审核通过，2表示审核中，3表示审核不通过',
  `user_info_edit_status` int(11) DEFAULT '0' COMMENT '用户修改状态：1表示修改中，0表示正常',
  `id_card_num` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `id_card_pic` varchar(200) DEFAULT NULL COMMENT '身份证号正面照片',
  `ip` varchar(50) DEFAULT NULL COMMENT '最后一次登录ip',
  `ip_territory` varchar(50) DEFAULT NULL COMMENT 'IP属地',
  `edit_info_times` int(11) DEFAULT '3' COMMENT '本月剩余修改次数，每月3次，下月一号更新',
  `send_code_times` int(11) DEFAULT '3' COMMENT '发送次数，每日最多3次，凌晨12点刷新',
  `release_project_times` int(11) DEFAULT '10' COMMENT '今日剩余发布项目次数，每天10次，凌晨12点刷新',
  `project_help_times` int(11) DEFAULT '10' COMMENT '剩余项目助力次数，每天10次，凌晨12点刷新',
  `device_type` varchar(50) DEFAULT NULL COMMENT '登录设备类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_project_help_times` int(11) DEFAULT '10' COMMENT '今日剩余创建项目助力次数',
  `disable_status` int(11) DEFAULT '0' COMMENT '用户封禁状态:1表示封禁，0表示正常',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `version` bigint(50) DEFAULT '0' COMMENT '版本号',
  `audit_msg` varchar(50) DEFAULT NULL COMMENT '审核内容',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';

SET FOREIGN_KEY_CHECKS = 1;
