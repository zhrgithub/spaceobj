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

 Date: 29/10/2022 17:46:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_project
-- ----------------------------
BEGIN;
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (7, 'bf63f43c-11a1-4105-91b0-7232d99203e3', '测试内容1', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, 1, '上海奉贤', '测试', '23232323', '2022-09-08 23:48:46', '2022-10-29 01:59:09', 14);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (8, '2aeacc52-17b8-4c09-9b9b-c533d27bdcc6', '测试内容2', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 1, 1, '上海奉贤', '测试', '通过', '2022-09-08 23:48:50', '2022-10-29 01:59:41', 3);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (9, 'a4e82847-f71b-4b95-85d3-5f6666973443', '测试内容3', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, 1, '上海奉贤', '测试', '通过', '2022-09-08 23:48:54', '2022-09-10 01:25:09', 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (10, '6f1a4f59-eccb-4682-abde-66cbee3d59a2', '测试内容aaa', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 0, 0, '上海奉贤', '测试', '通过', '2022-09-09 19:41:17', '2022-10-29 02:17:31', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (11, 'e8406775-0367-4796-88e7-04db88e1dc6b', '测试内容555', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 7, 0, '上海奉贤', '测试', '通过', '2022-09-10 00:04:44', '2022-10-29 02:15:41', 8);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (12, '2a82f0e8-235c-4a93-a2f2-04ae5dcf9c93', '测试内容55566', 12.88, '2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 2, 0, '上海奉贤', '测试', '通过', '2022-09-10 00:05:27', '2022-10-29 02:14:44', 3);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (13, '5ea5aed7-0856-432a-853b-c1ae44d135ef', '测试', 22.20, '8e10b07d-0217-4bba-a8f5-979159d049d8', 9, 1, '上海市', '测试', '通过', '2022-09-13 01:28:22', '2022-10-29 02:17:15', 8);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (14, '9734bbba-3b26-40af-88c7-86438ca8d731', '测99', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:07:17', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (15, '536f391e-162c-49a6-9904-88a4b2cf55c3', '测99', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 3, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:07:20', '2022-10-27 20:13:12', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (16, 'ffb60e28-b8af-4f58-a308-df2411d84126', '测99', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:09:52', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (17, '2edeec76-5c7d-491d-8148-42a32a9e72d3', '1111111111111111111111111111', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:09:54', '2022-10-27 20:07:53', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (18, '9ce4b672-870f-4165-9594-64510518d977', '测99', 123456.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '213', '2022-09-24 18:10:12', '2022-10-27 20:07:17', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (19, '3b3ecb3e-cc0d-41c7-b28d-6e49aef4e4b1', '测99', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 3, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:10:42', '2022-10-27 20:13:01', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (20, '0cf2c494-9a57-4a70-8848-4c47d359634a', '测992121', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:10:52', '2022-10-27 20:04:00', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (21, '92ecf89c-990c-4f9c-b7a2-1a4f7c88b862', '测999', 12.88, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '上海奉贤', '测试', '审核中请稍后', '2022-09-24 18:15:10', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (22, 'eca1c7f8-5fec-4833-b7ac-b8746a7f2699', '需要俩美女主播帮我赚钱，帮我写代码，替我做饭洗衣服，他妈的 爽', 50.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '广东省深圳市 联通', 'undefined', '审核中请稍后', '2022-10-27 15:36:29', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (23, '82d90522-3ea8-45dd-8c9b-757b6539472b', '需要俩美女主播', 50.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '广东省深圳市 联通', 'undefined', '审核中请稍后', '2022-10-27 15:48:27', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (24, '3bf6ff30-cec8-4eee-bae8-f06a35e72eab', '21212121', 32323.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '广东省深圳市 联通', 'undefined', '审核中请稍后', '2022-10-27 16:08:34', '2022-10-27 20:04:54', 2);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (25, 'e228eee6-05f5-4a6a-a114-6c129d15eb93', '二位', 3232.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '广东省深圳市 联通', 'spaceObj', '审核中请稍后', '2022-10-27 17:21:57', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (26, '05db9f46-8caa-4021-ac10-4aa3c3d31f2b', '为搜索', 323.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 0, '广东省深圳市 联通', 'spaceObj', '审核中请稍后', '2022-10-27 17:22:56', NULL, 0);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (27, '0f12bd0e-4a19-4b67-a804-3043fb5b46e3', '32323', 22323.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 0, 1, '广东省深圳市 联通', 'spaceObj', '审核中请稍后', '2022-10-27 18:18:13', '2022-10-29 02:22:15', 1);
INSERT INTO `sys_project` (`p_id`, `p_uuid`, `p_content`, `p_price`, `p_release_user_id`, `p_page_views`, `p_status`, `p_ip_address`, `p_nick_name`, `p_message`, `create_time`, `update_date`, `version`) VALUES (28, 'a3608380-db52-4d38-b65d-12c2b59d5861', '插上了课程是', 323.00, '8e10b07d-0217-4bba-a8f5-979159d049d8', 6, 1, '广东省深圳市 联通', 'spaceObj', '审核中请稍后', '2022-10-27 18:31:06', '2022-10-29 15:45:05', 7);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
