SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `channel_name` varchar(30) NOT NULL COMMENT '渠道名称',
  `channel_type` varchar(20) NOT NULL COMMENT 'aliyun：阿里云 / emay:   亿美 ',
  `channel_appkey` varchar(80) NOT NULL COMMENT '渠道用户名',
  `channel_appsecret` varchar(80) NOT NULL COMMENT '渠道密码',
  `channel_domain` varchar(80) NOT NULL COMMENT '渠道请求地址',
  `ext_properties` varchar(200) NOT NULL COMMENT '备用参数',
  `status` tinyint(4) NOT NULL COMMENT '状态0：启用 1：禁用',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `send_order` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
  `sign_name` varchar(20) NOT NULL COMMENT '签名',
  `template_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：验证码。\n1：短信通知。\n2：推广短信。\n3：国际/港澳台消息',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 0：有效 1：无效',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=521784428500447235 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_template
-- ----------------------------
BEGIN;
INSERT INTO `t_sms_template` (`id`, `template_name`, `content`, `sign_name`, `template_type`, `status`, `create_time`, `update_time`, `remark`) VALUES (521784428500447234, '测试模版', '您的验证码是：${code}', '测试', 0, 0, '2023-07-15 20:26:27', '2023-07-15 20:26:27', '你好');
COMMIT;

-- ----------------------------
-- Table structure for t_sms_template_binding
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template_binding`;
CREATE TABLE `t_sms_template_binding` (
  `id` bigint(20) NOT NULL,
  `template_id` bigint(20) NOT NULL COMMENT '模版编号',
  `template_code` varchar(35) COLLATE utf8_bin NOT NULL COMMENT '三方模版编码',
  `template_content` varchar(500) COLLATE utf8_bin NOT NULL COMMENT '三方模版内容',
  `channel_id` bigint(20) NOT NULL COMMENT '渠道编号',
  `status` tinyint(4) NOT NULL COMMENT '0 : 待提交 1：待审核  2：审核成功 3：审核失败',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sms_template_binding
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
