/*
 Navicat Premium Data Transfer

 Source Server         : localhostMySQL
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : spaceobj-advertise

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 29/10/2022 17:46:00
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

SET FOREIGN_KEY_CHECKS = 1;
