/*
 Navicat Premium Data Transfer

 Source Server         : localhostMySQL
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : spaceobj-user

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 29/10/2022 17:46:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Records of sys_photo
-- ----------------------------
BEGIN;
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('-1715982334', 'https://img2.baidu.com/it/u=1893470775,4143435497&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', '2022-10-24 14:12:38', '2022-10-28 20:11:38');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('-654823423', 'https://img0.baidu.com/it/u=2371305810,3587591415&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281', '2022-10-24 14:13:16', '2022-10-28 20:12:01');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('1', 'https://img2.baidu.com/it/u=720711137,2832039973&fm=253&fmt=auto&app=120&f=JPEG?w=640&h=960', '2022-09-28 14:32:07', '2022-10-28 20:12:15');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('1282945025', 'https://img2.baidu.com/it/u=2983827435,378917087&fm=253&fmt=auto&app=138&f=JPEG?w=374&h=500', '2022-10-24 14:12:03', '2022-10-28 20:12:25');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('1354248193', 'https://img1.baidu.com/it/u=3206351602,2714439067&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500', '2022-10-24 14:13:02', '2022-10-28 20:12:54');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('1421357057', 'https://img2.baidu.com/it/u=1114729443,1120710416&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', '2022-10-24 14:12:31', '2022-10-28 20:13:12');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('38fea5da6e6f4fa3922c852dad713749', 'https://img2.baidu.com/it/u=2436080376,661021775&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=888', '2022-10-24 19:54:12', '2022-10-28 20:13:27');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('41431042', 'https://img0.baidu.com/it/u=1915308837,2015938396&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', '2022-10-24 14:12:45', '2022-10-28 20:13:41');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('4f226bce455a594722a4b763b046c5a7', 'https://img1.baidu.com/it/u=2658939207,2791674017&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', '2022-10-27 22:43:49', '2022-10-28 20:13:56');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('5fa5ef4f874fb756817d6d76e738186e', 'https://img0.baidu.com/it/u=2396877489,2202564956&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281', '2022-10-27 22:46:38', '2022-10-28 20:14:10');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('6ebb4a6887bf0226fdfc3b7bfb99ec67', 'https://img0.baidu.com/it/u=2253583263,1204097971&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500', '2022-10-25 23:05:17', '2022-10-28 20:14:22');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('84683e652a159b136bd046b7aca8f357', 'https://img1.baidu.com/it/u=745998857,124348167&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281', '2022-10-27 22:48:02', '2022-10-28 20:14:45');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('c1781563175c7cc2dcafae36fc57afc5', 'https://img2.baidu.com/it/u=3437519712,2889146289&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=500', '2022-10-24 18:45:17', NULL);
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('cab260f634ac96571c1b7f6253583add', 'https://img0.baidu.com/it/u=1088754973,1390499664&fm=253&fmt=auto&app=138&f=JPEG?w=335&h=500', '2022-10-27 22:50:46', '2022-10-28 20:11:18');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('ccc8b54b6523a9eb45e7e812766b3fa9', 'https://img0.baidu.com/it/u=922902802,2128943538&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800', '2022-10-27 22:47:33', '2022-10-28 20:11:03');
INSERT INTO `sys_photo` (`photo_id`, `photo_url`, `create_time`, `update_time`) VALUES ('f19e6f493f18d96b8660ced4140dacf5', 'https://img1.baidu.com/it/u=3009731526,373851691&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500', '2022-10-27 22:51:50', '2022-10-28 20:10:44');
COMMIT;

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

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('16bd345b-ad9d-4bb0-9b84-4b33c4b982c0', 'null', 'csca@163.com', 'null', 'e10adc3949ba59abbe56e057f20f883e', '94af4653-5bd9-4dad-9bf5-7c1cfd3a7d9b', 'null', '1322222222', 0, 0, 'null', 'null', '哈哈11', '测试11', 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 1, 1, 0, 'null', 'null', '192.168.0.114', '上海市 联通', 3, 3, 10, 10, 'iPhone 6/7/8', '2022-09-16 22:33:23', '2022-10-29 02:22:33', 10, 0, 'zhr_java@163.com', 0, 'null');
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('2659b44c-f4aa-4ed8-83bf-8f5a782e7cbf', 'null', 'ccc@qq.com', 'null', 'e10adc3949ba59abbe56e057f20f883e', '8520d621-fa2f-458e-99df-00800af69902', 'null', '13212346439', 0, 0, 'null', 'null', '吼吼111', '1112', 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 1, 2, 0, 'null', 'null', '127.0.0.1', '上海市', 3, 3, 10, 10, 'MacBook Pro', '2022-09-09 19:41:06', '2022-10-27 23:25:40', 10, 0, '11', 0, 'null');
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 'null', 'bbb@qq.com', 'null', 'e10adc3949ba59abbe56e057f20f883e', '8fc52f2a-5ddd-4b6b-80c9-4ca3936190ce', 'null', '13212346439', 0, 0, 'null', 'null', '11', '快乐的张三', 'https://img0.baidu.com/it/u=1915308837,2015938396&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', 0, 2, 0, 'null', 'null', '172.20.10.2', '广东省深圳市 联通', 1, 3, 10, 10, 'iPhone 6/7/8', '2022-09-08 23:48:30', '2022-10-27 23:25:40', 10, 0, 'ccc@qq.com', 13, NULL);
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('5eb8b88d-b7b4-4cd6-a66b-b3a03be29f11', 'null', 'zhr_java2@163.com', 'null', 'e10adc3949ba59abbe56e057f20f883e', '7e096538-f860-4585-a899-1287da0ee448', 'null', '13212346439', 0, 0, 'null', 'null', '11', '11', 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 1, 2, 0, 'null', 'null', '172.20.10.2', '上海市', 3, 3, 10, 10, 'MacBook Pro', '2022-09-15 15:53:11', '2022-10-27 23:25:40', 10, 0, 'zhr_java@163.com', 0, NULL);
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('8e10b07d-0217-4bba-a8f5-979159d049d8', '', 'zhr_java@163.com', '937331', '96e79218965eb72c92a549dd5a330112', '070644b7-da80-42ac-adb6-805933340d37', 'ocnPE5CvJ1kE-HbMKmAE0xRrup4Q', '13212346939', 0, 99, 'root', 'user,other,advertise,project', '张泓锐', 'spaceObj', 'https://img2.baidu.com/it/u=1114729443,1120710416&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', 0, 1, 0, '130427199600000000', '/Users/zhr/Downloads/20221026191037-d2855dc1-f02e-480e-ac77-669adf022a56.jpg', '172.20.10.2', '广东省深圳市 联通', 959, 1, 10, 9, 'iPhone 6/7/8', '2022-09-10 00:27:28', '2022-10-29 15:43:17', 10, 0, 'zhr_java@163.com', 26, '身份证不清晰');
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('cb7b3a1e-1415-4970-8f24-09d7da024ac8', NULL, 'abc@163.com', NULL, 'e10adc3949ba59abbe56e057f20f883e', '42e77420-252e-4e1d-a8d0-68ae4ffc74f3', NULL, '13212346439', 0, 0, NULL, NULL, NULL, NULL, 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 1, 2, 0, NULL, NULL, '127.0.0.1', '上海市', 3, 3, 10, 10, 'MacBook Pro', '2022-09-10 15:38:20', '2022-10-27 23:25:40', 10, 0, NULL, 0, NULL);
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('f2b60761-7eec-426d-ba06-6f3bc780d44e', NULL, 'dsadc@163.com', NULL, 'e10adc3949ba59abbe56e057f20f883e', '7649670c-4408-4488-a496-57f12fe57a09', NULL, '13232322323', 0, 0, NULL, NULL, NULL, NULL, 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 1, 2, 0, NULL, NULL, '192.168.0.114', '上海市 联通', 3, 3, 10, 10, 'iPhone 6/7/8', '2022-09-18 12:56:56', '2022-10-27 23:25:40', 10, 0, NULL, 0, NULL);
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('f74e9cb1-7eda-44ef-9cdd-5d831ffdce57', NULL, 'spaceobj@163.com', '030581', 'e10adc3949ba59abbe56e057f20f883e', 'e6eee4ca-5464-4d6d-95b0-3542df819ef3', NULL, '13237434384', 0, 0, NULL, NULL, NULL, NULL, 'https://img0.baidu.com/it/u=846799635,2112893415&fm=253&fmt=auto&app=138&f=JPEG?w=434&h=640', 0, 2, 0, NULL, NULL, '192.168.0.114', '上海市 联通', 3, 3, 10, 10, 'iPhone 6/7/8', '2022-09-16 22:31:03', '2022-10-27 23:25:40', 10, 0, NULL, 0, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
