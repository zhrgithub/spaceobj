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

 Date: 31/10/2022 00:07:17
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
-- Records of jd_advertise
-- ----------------------------
BEGIN;
INSERT INTO `jd_advertise` (`jd_ad_id`, `jd_ad_hyperlink`, `jd_ad_image_link`, `jd_ad_name`, `jd_ad_store_name`, `jd_ad_coupon`, `jd_ad_price`, `jd_ad_comment_num`, `jd_ad_create_time`, `jd_ad_update_time`) VALUES (-1216761855, 'https://item.jd.com/10056426707425.html', 'https://img10.360buyimg.com/n1/jfs/t1/23860/33/17905/222376/62d181c3E2dc499a3/f1a79117eba68f4d.jpg.avif', '烟薯25号红薯 糖心蜜薯 烤红薯地瓜 健康轻食 新鲜水果蔬菜生鲜 精选3斤', '寻鲜记专营店', ' 购买不超过20件时享受单件价￥9.90', 1000.00, '12', '2022-09-28 21:05:59', '2022-10-07 13:56:18');
INSERT INTO `jd_advertise` (`jd_ad_id`, `jd_ad_hyperlink`, `jd_ad_image_link`, `jd_ad_name`, `jd_ad_store_name`, `jd_ad_coupon`, `jd_ad_price`, `jd_ad_comment_num`, `jd_ad_create_time`, `jd_ad_update_time`) VALUES (1, 'https://item.jd.com/10028959766071.html', 'https://img11.360buyimg.com/n1/jfs/t1/168928/12/26983/398301/61badc0dE70e6c50d/07d36fa0c2abf9b4.jpg', '千丝肉松饼整箱500g ', '中国特产·泉州馆', '购买不超过200件时享受单件价￥8.90', 32.00, '10', '2022-09-10 02:51:00', '2022-09-28 20:58:07');
INSERT INTO `jd_advertise` (`jd_ad_id`, `jd_ad_hyperlink`, `jd_ad_image_link`, `jd_ad_name`, `jd_ad_store_name`, `jd_ad_coupon`, `jd_ad_price`, `jd_ad_comment_num`, `jd_ad_create_time`, `jd_ad_update_time`) VALUES (2, 'https://item.jd.com/10023433160986.html', 'https://img11.360buyimg.com/n1/jfs/t1/126829/25/11015/116254/5f48aaa7E4fc5dc94/990e9acfada642f9.jpg', '甘肃天水花牛 国产红蛇果 新鲜苹果水果 2斤', '中国特产', '每满300元，可减30元现金', 9.90, '100', '2022-09-13 00:52:43', '2022-09-28 21:09:58');
INSERT INTO `jd_advertise` (`jd_ad_id`, `jd_ad_hyperlink`, `jd_ad_image_link`, `jd_ad_name`, `jd_ad_store_name`, `jd_ad_coupon`, `jd_ad_price`, `jd_ad_comment_num`, `jd_ad_create_time`, `jd_ad_update_time`) VALUES (121180161, 'https://img2.baidu.com/it/u=1893470775,4143435497&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', 'https://img2.baidu.com/it/u=1893470775,4143435497&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', '111', '11', '11', 11.00, '11', '2022-10-07 14:20:12', NULL);
COMMIT;

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
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('2e93dc8e-9b47-4c2c-bca0-1f3c97805571', 'null', 'bbb@qq.com', 'null', 'e10adc3949ba59abbe56e057f20f883e', 'c820c9f6-3a5f-4cf3-b107-0f8baf6d68d5', 'null', '13212346439', 0, 0, 'null', 'null', '11', '快乐的张三', 'https://img0.baidu.com/it/u=1915308837,2015938396&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', 1, 2, 0, 'null', 'null', '172.20.10.2', '中国广东深圳', 1, 3, 10, 10, 'iPhone 6/7/8', '2022-09-08 23:48:30', '2022-10-30 22:56:05', 9, 0, 'ccc@qq.com', 19, NULL);
INSERT INTO `sys_user` (`user_id`, `invite_user_id`, `account`, `email_code`, `password`, `token`, `open_id`, `phone_number`, `assist_value`, `invitation_value`, `user_type`, `user_rights`, `username`, `nick_name`, `photo_url`, `online_status`, `real_name_status`, `user_info_edit_status`, `id_card_num`, `id_card_pic`, `ip`, `ip_territory`, `edit_info_times`, `send_code_times`, `release_project_times`, `project_help_times`, `device_type`, `create_time`, `update_time`, `create_project_help_times`, `disable_status`, `email`, `version`, `audit_msg`) VALUES ('6b2186e7-e050-47f4-8a39-06c858b8f4a8', NULL, 'ocnPE5CvJ1kE-HbMKmAE0xRrup4Q', NULL, NULL, '9dc0b0ac-150f-4ae5-9325-cdb584f510b7', 'ocnPE5CvJ1kE-HbMKmAE0xRrup4Q', '13212346439', 0, 0, 'root', 'root,user,project,other,advertise', NULL, '张三', 'https://img0.baidu.com/it/u=1915308837,2015938396&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', 0, 0, 0, NULL, NULL, '172.20.10.2', '中国广东深圳', 0, 3, 9, 9, NULL, '2022-10-30 22:19:05', '2022-10-30 22:51:57', 10, 0, 'zhr_java@163.com', 16, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
