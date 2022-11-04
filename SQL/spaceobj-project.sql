/*
 Navicat Premium Data Transfer

 Source Server         : localhostMySQL
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : spaceobj-project

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 31/10/2022 00:07:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project_help
-- ----------------------------
DROP TABLE IF EXISTS `project_help`;
CREATE TABLE `project_help` (
  `hp_id` varchar(50) NOT NULL COMMENT '项目助力UUID',
  `p_uuid` varchar(50) NOT NULL COMMENT '项目UUID',
  `create_user_id` varchar(50) NOT NULL COMMENT '创建人ID',
  `hp_number` bigint(50) NOT NULL DEFAULT '0' COMMENT '被助力次数',
  `p_content` text NOT NULL COMMENT '项目内容',
  `p_price` decimal(50,2) NOT NULL COMMENT '项目预算',
  `p_release_user_id` varchar(50) NOT NULL COMMENT '项目发布人ID',
  `hp_status` int(10) NOT NULL DEFAULT '0' COMMENT '助力状态：0表示助力中，1表示助力成功，2表示已删除，3表示已成交，默认0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(50) DEFAULT '0' COMMENT '版本号',
  `ip_territory` varchar(50) DEFAULT NULL COMMENT '项目助力创建人的ip属地',
  `hp_create_nick_name` varchar(50) DEFAULT NULL COMMENT '项目助力创建人的昵称',
  `project_create_nick_name` varchar(50) DEFAULT NULL COMMENT '项目创建人的昵称',
  PRIMARY KEY (`hp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_help
-- ----------------------------
BEGIN;
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('a0d0b60b-45d9-43c6-859b-5750a836b4e0', 'fad32851-2545-420f-8db5-083c29945015', '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '《拿破仑》是享誉世界的德国传记大师埃米尔·路德维希的代表作，是拿破仑传记中的经典作品之一。本书在美国出版后在“非虚构类畅销书排行榜”上位列榜首。本书描述了拿破仑叱咤风云的一生中经历过的各次重大战役及其辉煌的军事成就。拿破仑代表一个时代，被称为一代“军事巨人”，同时也是一个伟大的政治天才。作者通过翔实的史料，将拿破仑的伟大魅力和历史功过真实生动地展现在读者面前。史实与细致入微的心理刻画，以及栩栩如生的人物描写的完美结合使本书兼具文学性与可读性。', 100.00, '6b2186e7-e050-47f4-8a39-06c858b8f4a8', 0, '2022-10-30 22:37:14', '2022-10-30 22:50:29', 1, '中国广东深圳', '快乐的张三', '张三');
COMMIT;

-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project` (
  `p_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `p_uuid` varchar(50) NOT NULL COMMENT '项目临时唯一编号',
  `p_content` text NOT NULL COMMENT '项目内容',
  `p_price` decimal(50,2) NOT NULL COMMENT '项目预算',
  `p_release_user_id` varchar(50) NOT NULL COMMENT '发布人id',
  `p_page_views` bigint(50) DEFAULT '0' COMMENT '项目浏览量',
  `p_status` bigint(50) NOT NULL DEFAULT '0' COMMENT '项目状态，0表示审核中，1表示审核通过，2表示审核不通过，3表示已删除，4已成交，默认0',
  `p_ip_address` varchar(50) DEFAULT NULL COMMENT 'IP属地',
  `p_nick_name` varchar(50) NOT NULL COMMENT '用户昵称',
  `p_message` varchar(50) DEFAULT '审核中请稍后' COMMENT '审核内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint(50) NOT NULL DEFAULT '0' COMMENT '版本号，每次修改增加1，初始值为0',
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_project
-- ----------------------------
BEGIN;
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (29, 'fad32851-2545-420f-8db5-083c29945015', '《拿破仑》是享誉世界的德国传记大师埃米尔·路德维希的代表作，是拿破仑传记中的经典作品之一。本书在美国出版后在“非虚构类畅销书排行榜”上位列榜首。本书描述了拿破仑叱咤风云的一生中经历过的各次重大战役及其辉煌的军事成就。拿破仑代表一个时代，被称为一代“军事巨人”，同时也是一个伟大的政治天才。作者通过翔实的史料，将拿破仑的伟大魅力和历史功过真实生动地展现在读者面前。史实与细致入微的心理刻画，以及栩栩如生的人物描写的完美结合使本书兼具文学性与可读性。', 100.00, '6b2186e7-e050-47f4-8a39-06c858b8f4a8', 1, 1, '中国广东深圳', '张三', '审核中请稍后', '2022-10-30 22:29:38', '2022-10-30 22:37:10', 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
