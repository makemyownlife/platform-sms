/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : tech_platform

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 02/05/2023 12:20:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_platform_appinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_appinfo`;
CREATE TABLE `t_platform_appinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) NOT NULL COMMENT '应用名称',
  `app_key` varchar(100) NOT NULL,
  `app_secret` varchar(100) NOT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0 : 有效  /  1: 无效',
  `remark` varchar(255) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_app_name_indexkey` (`app_name`) USING BTREE COMMENT 'app_name 全局唯一',
  UNIQUE KEY `unique_app_key_indexkey` (`app_key`) USING BTREE COMMENT 'app_key 全局唯一'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_platform_appinfo
-- ----------------------------
BEGIN;
INSERT INTO `t_platform_appinfo` (`id`, `app_name`, `app_key`, `app_secret`, `status`, `remark`, `create_time`, `update_time`) VALUES (1, 'scheduleconsole', '140002', '1000', 0, NULL, '2019-11-06 17:08:03', '2019-11-06 17:08:03');
INSERT INTO `t_platform_appinfo` (`id`, `app_name`, `app_key`, `app_secret`, `status`, `remark`, `create_time`, `update_time`) VALUES (2, 'com.courage.test22', '140001', '1001', 0, NULL, '2019-11-06 17:08:31', '2019-11-06 17:08:31');
INSERT INTO `t_platform_appinfo` (`id`, `app_name`, `app_key`, `app_secret`, `status`, `remark`, `create_time`, `update_time`) VALUES (3, 'usercenter', '140003', '1002', 0, '用户中心', '2019-11-12 11:41:30', '2019-12-02 15:36:33');
INSERT INTO `t_platform_appinfo` (`id`, `app_name`, `app_key`, `app_secret`, `status`, `remark`, `create_time`, `update_time`) VALUES (4, 'mytest', '140004', '76f620b1c958a5ea5eddc53aea931e88', 0, 'haha哈哈哈 ', '2019-12-02 15:42:15', '2019-12-02 15:42:15');
COMMIT;

-- ----------------------------
-- Table structure for t_platform_namesrv
-- ----------------------------
DROP TABLE IF EXISTS `t_platform_namesrv`;
CREATE TABLE `t_platform_namesrv` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键Id',
  `namesrv_ip` varchar(80) NOT NULL COMMENT 'namesrv ip ;分隔',
  `status` tinyint(4) NOT NULL COMMENT '状态 0：有效 1：无效',
  `type` tinyint(4) DEFAULT NULL COMMENT '0: 任务调度 ',
  `role` tinyint(4) DEFAULT '0' COMMENT '0: master 1: slave',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_namesrv_ip_key` (`namesrv_ip`) USING BTREE COMMENT 'ip 唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_platform_namesrv
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_schedule_job_info
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job_info`;
CREATE TABLE `t_schedule_job_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL COMMENT '应用编号',
  `app_name` varchar(80) COLLATE utf8_bin NOT NULL COMMENT '应用名称',
  `job_name` varchar(80) COLLATE utf8_bin NOT NULL COMMENT '任务名称',
  `job_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务类别\n0: 基础定时任务',
  `job_description` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '任务描述',
  `job_cron` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'cron表达式',
  `job_param` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '任务参数',
  `job_handler` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '任务处理器 当前job_type:0 时，默认是java类上的注解',
  `route_mode` tinyint(255) DEFAULT '0' COMMENT '是否广播模式 0：不是 1： 是',
  `author` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '负责人',
  `alarm_email` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '报警邮箱',
  `status` tinyint(4) DEFAULT '0' COMMENT ' 0: 有效 1: 无效 ',
  `trigger_next_time` datetime DEFAULT NULL COMMENT '下次触发时间',
  `trigger_last_time` datetime DEFAULT NULL COMMENT '最后触发时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `normal_create_time_key` (`create_time`) USING BTREE COMMENT '创建时间',
  KEY `normal_app_id_key` (`app_id`) USING BTREE COMMENT '应用id',
  KEY `unique_job_name` (`job_name`) USING BTREE COMMENT '任务名称'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_schedule_job_info
-- ----------------------------
BEGIN;
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (4, 1000, 'scheduleconsole', '远方的人', 0, '', '0,2,12 * * * * ?', '林 hello', 'demo', 0, '张勇', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-11-06 18:54:57', '2019-11-07 09:55:40');
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (6, 1001, 'com.courage.test22', '高慧的样子123', 0, '', '0/5 * * * * ?', '', 'demo2.test23.mytime', 0, '张勇113林', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-11-06 21:44:47', '2019-11-07 09:55:35');
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (7, 1000, 'scheduleconsole', '林妹妹啊', 0, '', '0/5 * * * * ?', '{}', 'demo.test3333', 0, '张勇', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-11-06 23:03:41', '2019-11-11 12:45:26');
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (8, 1000, 'scheduleconsole', '订单列表查询', 0, '', '0/10 */2 * * * ?', '', 'orderList.query', 0, '张志峰', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-11-06 23:12:40', '2019-11-12 14:20:30');
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (9, 1000, 'scheduleconsole', '史湘云', 0, '', '0,12 * * * * ?', '22222我的东西', 'mytest', 0, '张勇113林', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-11-06 23:56:31', '2019-11-13 15:29:11');
INSERT INTO `t_schedule_job_info` (`id`, `app_id`, `app_name`, `job_name`, `job_type`, `job_description`, `job_cron`, `job_param`, `job_handler`, `route_mode`, `author`, `alarm_email`, `status`, `trigger_next_time`, `trigger_last_time`, `create_time`, `update_time`) VALUES (10, 2, 'com.courage.test22', '修改后', 0, '', '0,2,12 * * * * ?', '{}哈哈', 'demo2.test', 0, 'zhangyong', 'zhangyong7120180@163.com', 1, NULL, NULL, '2019-12-02 15:43:24', '2019-12-02 15:43:33');
COMMIT;

-- ----------------------------
-- Table structure for t_schedule_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job_lock`;
CREATE TABLE `t_schedule_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_schedule_job_lock
-- ----------------------------
BEGIN;
INSERT INTO `t_schedule_job_lock` (`lock_name`) VALUES ('schedule_lock');
COMMIT;

-- ----------------------------
-- Table structure for t_schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job_log`;
CREATE TABLE `t_schedule_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用id',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '-1: 待触发\n0: 触发成功\n1: 触发失败',
  `trigger_time` datetime DEFAULT NULL COMMENT '触发时间',
  `trigger_message` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '触发内容',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `callback_status` tinyint(4) DEFAULT '-1' COMMENT '回调状态 0：成功 1：失败 2： 部分失败 -1 : 未回调 ',
  `callback_message` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '回调内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `normal_job_id_key` (`job_id`) USING BTREE COMMENT '任务id',
  KEY `normal_app_id_key` (`app_id`) USING BTREE COMMENT '应用id',
  KEY `normal_create_time_key` (`create_time`) USING BTREE COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_schedule_job_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sms_appinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_appinfo`;
CREATE TABLE `t_sms_appinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(20) NOT NULL,
  `app_name` varchar(40) NOT NULL DEFAULT '',
  `app_secret` varchar(100) NOT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0 : 有效  /  1: 无效',
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_appinfo
-- ----------------------------
BEGIN;
INSERT INTO `t_sms_appinfo` (`id`, `app_key`, `app_name`, `app_secret`, `status`, `remark`, `create_time`, `update_time`) VALUES (1, 'qQjEiFzn80v8VM4h', 'mydemo', '9c465ece754bd26a9be77f3d0e2606bd', 0, '例子', '2023-05-02 12:09:55', '2023-05-02 12:09:55');
COMMIT;

-- ----------------------------
-- Table structure for t_sms_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_channel`;
CREATE TABLE `t_sms_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `channel_type` varchar(20) NOT NULL COMMENT 'aliyun：阿里云 / emay:   亿美 ',
  `channel_appkey` varchar(80) NOT NULL COMMENT '渠道用户名',
  `channel_appsecret` varchar(80) NOT NULL COMMENT '渠道密码',
  `channel_domain` varchar(80) NOT NULL COMMENT '渠道请求地址',
  `sign_name` varchar(20) DEFAULT NULL COMMENT '签名',
  `ext_properties` varchar(200) NOT NULL COMMENT '备用参数',
  `status` tinyint(4) NOT NULL COMMENT '状态0：启用 1：禁用',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `send_order` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_channel
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sms_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_record`;
CREATE TABLE `t_sms_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 自增',
  `sms_id` varchar(40) NOT NULL COMMENT '短信id',
  `mobile` text NOT NULL COMMENT '手机号',
  `nationcode` varchar(10) NOT NULL DEFAULT '86' COMMENT '区位码',
  `app_id` varchar(30) DEFAULT NULL COMMENT '调用服务方',
  `content` varchar(500) NOT NULL COMMENT '短信内容',
  `type` tinyint(4) DEFAULT '0' COMMENT '0：普通短信  1 ：营销短信 （群发）',
  `channel` varchar(10) NOT NULL COMMENT '渠道',
  `attime` varchar(30) DEFAULT '' COMMENT '指定发送时间',
  `send_status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '-1：待发送 / 0：已发送  /1  : 发送失败',
  `sender_ip` varchar(40) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sms_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_record_detail`;
CREATE TABLE `t_sms_record_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增Id',
  `record_id` bigint(20) NOT NULL COMMENT 't_sms_record表外键',
  `content` varchar(500) NOT NULL COMMENT '短信内容',
  `report_status` tinyint(4) DEFAULT NULL COMMENT '短信报告状态',
  `report_time` datetime DEFAULT NULL COMMENT '短信状态报告时间',
  `mobile` varchar(40) NOT NULL COMMENT '手机号码',
  `msgid` varchar(40) NOT NULL COMMENT '短信ID',
  `channel` varchar(10) NOT NULL COMMENT '渠道',
  `app_key` varchar(30) NOT NULL COMMENT '发送方appkey',
  `sender_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息发送时间',
  `receive_time` datetime DEFAULT NULL COMMENT '短信接收时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `send_status` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '-1：待发送 / 0：已发送  /1 : 发送失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_record_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template`;
CREATE TABLE `t_sms_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板id，主键自动增加',
  `template_name` varchar(40) NOT NULL COMMENT '模板名称',
  `content` varchar(500) NOT NULL COMMENT '模板内容',
  `channel` varchar(20) NOT NULL COMMENT '渠道id‘，’分隔',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 0：无效 1：有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_template
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
