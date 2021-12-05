/*
 Navicat Premium Data Transfer

 Source Server         : docker-mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : spring-tx-invalidation

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 05/12/2021 21:34:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `available` int(0) NULL DEFAULT 1 COMMENT '是否可用，1 可用，0 不可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除，0 未删除， 1 删除',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `amount` int(0) NULL DEFAULT NULL COMMENT '账户金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账户金额信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_info
-- ----------------------------
INSERT INTO `account_info` VALUES (1, 1, '2021-11-30 22:07:38', 0, '2021-12-05 21:30:51', 1000);
INSERT INTO `account_info` VALUES (2, 1, '2021-11-30 22:07:38', 0, '2021-12-05 21:30:51', 1000);

SET FOREIGN_KEY_CHECKS = 1;
