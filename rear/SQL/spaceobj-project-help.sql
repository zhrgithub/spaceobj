/*
 Navicat Premium Data Transfer

 Source Server         : localhostMySQL
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : spaceobj-project-help

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 29/10/2022 17:46:40
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
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('18f1abb0-b659-4860-bc24-c2fc0b90615e', '7', '8e10b07d-0217-4bba-a8f5-979159d049d8', 10, '测试内容1', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-13 01:34:21', '2022-10-27 23:18:36', 0, '河南', '张三', '孙二娘');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('453fdf94-3789-429c-a92f-95edc3541b26', '9', '8e10b07d-0217-4bba-a8f5-979159d049d8', 10, '测试内容3', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-13 01:32:29', '2022-10-27 23:18:41', 0, '安徽', '李四', '姜嗨');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('88beda2d-5350-4b0b-9738-079819cac4e3', '10', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 10, '测试内容aaa', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-10 00:03:45', '2022-10-27 23:18:45', 0, '河北', '王五', '郑旭');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('9a09faf2-872e-4da7-9000-82babb299e2a', '8', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 0, '测试内容2', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, '2022-09-10 00:03:31', '2022-10-27 23:18:48', 0, '上海', '赵六', '周十');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('a71602c0-26ac-4d9e-845e-c972f109901b', '12', '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, '测试内容55566', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, '2022-09-10 01:11:43', '2022-10-27 23:18:53', 0, '广东', '钱七', '王八');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('b79b8a3b-533c-4c49-8e9a-a7752f1da947', '8', '8e10b07d-0217-4bba-a8f5-979159d049d8', 10, '测试内容2', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-13 01:34:36', '2022-10-27 23:18:58', 0, '海南', '王八', '钱七');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('c40ac69f-3023-41a9-84d6-5fc725a41f47', '7', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 10, '测试内容1', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-10 00:02:45', '2022-10-27 23:19:02', 0, '深圳', '周十', '赵六');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('ce1a3c07-143d-4854-9fbe-eee85c2b4fe1', 'a3608380-db52-4d38-b65d-12c2b59d5861', '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 2, '插上了课程是', 323.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, '2022-10-28 22:58:35', '2022-10-29 02:21:50', 2, '广东省深圳市 联通', '快乐的张三', 'spaceObj');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('dd123cfa-b57b-405e-bfe7-e448364f4917', '11', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 0, '测试内容555', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, '2022-09-10 00:05:02', '2022-10-27 23:19:05', 0, '北京', '郑旭', '王五');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('dd72b7e9-51b6-4cf3-9b30-4f31cc091fd4', '9', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 0, '测试内容3', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, '2022-09-10 00:03:41', '2022-10-27 23:19:09', 0, '南京', '姜嗨', '李四');
INSERT INTO `project_help` (`hp_id`, `p_uuid`, `create_user_id`, `hp_number`, `p_content`, `p_price`, `p_release_user_id`, `hp_status`, `create_time`, `update_time`, `version`, `ip_territory`, `hp_create_nick_name`, `project_create_nick_name`) VALUES ('e6bed4b2-5fe8-4673-b1fb-02e522680544', '12', '2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 10, '测试内容55566', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, '2022-09-10 02:07:40', '2022-10-27 23:19:14', 0, '重庆', '孙二娘', '张三');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
