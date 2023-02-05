/*
 Navicat Premium Data Transfer

 Source Server         : myserver002
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 114.132.226.147:3306
 Source Schema         : spaceobj-project

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 05/11/2022 21:41:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project_help
-- ----------------------------
DROP TABLE IF EXISTS `project_help`;
CREATE TABLE `project_help` (
  `hp_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目助力UUID',
  `p_uuid` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目UUID',
  `create_user_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人ID',
  `hp_number` bigint(50) NOT NULL DEFAULT '0' COMMENT '被助力次数',
  `p_content` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目内容',
  `p_price` decimal(50,2) NOT NULL COMMENT '项目预算',
  `p_release_user_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目发布人ID',
  `hp_status` int(10) NOT NULL DEFAULT '0' COMMENT '助力状态：0表示助力中，1表示助力成功，2表示已删除，3表示已成交，默认0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(50) DEFAULT '0' COMMENT '版本号',
  `ip_territory` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目助力创建人的ip属地',
  `hp_create_nick_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目助力创建人的昵称',
  `project_create_nick_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目创建人的昵称',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  PRIMARY KEY (`hp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project` (
  `p_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `p_uuid` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目临时唯一编号',
  `p_content` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目内容',
  `p_price` decimal(50,2) NOT NULL COMMENT '项目预算',
  `p_release_user_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发布人id',
  `p_page_views` bigint(50) DEFAULT '0' COMMENT '项目浏览量',
  `p_status` bigint(50) NOT NULL DEFAULT '0' COMMENT '项目状态，0表示审核中，1表示审核通过，2表示审核不通过，3表示已删除，4已成交，默认0',
  `p_ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP属地',
  `p_nick_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `p_message` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '审核中请稍后' COMMENT '审核内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(50) NOT NULL DEFAULT '0' COMMENT '版本号，每次修改增加1，初始值为0',
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
