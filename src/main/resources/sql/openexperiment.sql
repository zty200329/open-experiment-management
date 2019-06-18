# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 118.24.9.53 (MySQL 5.7.21-0ubuntu0.16.04.1)
# Database: openexperiment
# Generation Time: 2019-06-18 15:38:41 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table acl
# ------------------------------------------------------------

CREATE TABLE `acl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL COMMENT '对权限的具体描述',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `url` varchar(255) DEFAULT NULL COMMENT '具体接口的url',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table announcement
# ------------------------------------------------------------

CREATE TABLE `announcement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `publisher_id` bigint(20) DEFAULT NULL COMMENT '发布者id',
  `title` varchar(255) DEFAULT NULL COMMENT '公告标题',
  `content` varchar(2000) DEFAULT NULL COMMENT '公告主要内容',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table funds
# ------------------------------------------------------------

CREATE TABLE `funds` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` int(11) DEFAULT NULL COMMENT '资金数额',
  `use` varchar(800) DEFAULT NULL COMMENT '具体用途',
  `type` int(11) DEFAULT NULL COMMENT '资金类型:1.材料费,2.资料,印刷费,3.出版费,4.教师酬金,5.其他',
  `applicant_id` bigint(20) DEFAULT NULL COMMENT '申请人id',
  `project_group_id` bigint(20) DEFAULT NULL COMMENT '项目组id',
  `status` int(11) DEFAULT NULL COMMENT '申请状态(1.申请中,2.批准,3.拒绝申请)',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table notice
# ------------------------------------------------------------

CREATE TABLE `notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '通知标题',
  `sender_id` bigint(20) DEFAULT NULL COMMENT '发送者id',
  `message` varchar(255) DEFAULT NULL COMMENT '具体消息',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table polemic_group
# ------------------------------------------------------------

CREATE TABLE `polemic_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '分组名称',
  `status` int(11) DEFAULT NULL COMMENT '答辩分组状态:1.立项答辩,2.中期答辩,3.结项答辩',
  `group_num` int(11) DEFAULT NULL COMMENT '一个答辩组限制的项目组数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project_file
# ------------------------------------------------------------

CREATE TABLE `project_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_group_id` bigint(20) DEFAULT NULL,
  `download_times` int(11) DEFAULT NULL COMMENT '下载次数',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_type` int(11) DEFAULT NULL COMMENT '文件类型：1.excel,2.word,3.video,4.image',
  `size` varchar(255) DEFAULT NULL COMMENT '文件大小',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `upload_user_id` bigint(20) DEFAULT NULL COMMENT '上传者id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project_group
# ------------------------------------------------------------

CREATE TABLE `project_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL COMMENT '实验地点',
  `create_time` datetime DEFAULT NULL COMMENT '小组申报时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '申报者id',
  `end_time` datetime DEFAULT NULL COMMENT '实验截止时间',
  `experiment_condition` varchar(800) DEFAULT NULL COMMENT '实验条件(描述)',
  `suggest_group_type` int(11) DEFAULT NULL COMMENT '建议评分分组:1.A组石工地堪,2.B组化工材料3.C组机械力学4.E组软件与数学,5.F组经管法律艺体人文',
  `experiment_type` int(11) DEFAULT NULL COMMENT '实验类型：1.科研，2.科技活动，3.自选课题，4.计算机应用，5.人文素质',
  `achievement_check` varchar(800) DEFAULT NULL COMMENT '成果考核方式',
  `limit_college` varchar(255) DEFAULT NULL COMMENT '限选学院',
  `limit_major` varchar(255) DEFAULT NULL COMMENT '限选专业',
  `limit_grade` int(11) DEFAULT NULL COMMENT '限选年级',
  `fit_people_num` int(11) DEFAULT NULL COMMENT '适宜人数',
  `total_hours` int(11) DEFAULT NULL COMMENT '总计划实验小时',
  `lab_name` varchar(255) DEFAULT NULL COMMENT '实验室名称',
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `project_type` int(11) DEFAULT NULL COMMENT '项目类型：1.普通，2.重点',
  `start_time` datetime DEFAULT NULL COMMENT '实验开始时间',
  `status` int(11) DEFAULT NULL COMMENT '实验开展进度，根据需求进行确定',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `project_name_index` (`project_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project_polemic
# ------------------------------------------------------------

CREATE TABLE `project_polemic` (
  `id` bigint(20) NOT NULL,
  `project_group_id` bigint(20) DEFAULT NULL COMMENT '项目组id',
  `polemic_group_id` bigint(20) DEFAULT NULL COMMENT '答辩组id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table receive_notice
# ------------------------------------------------------------

CREATE TABLE `receive_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notice_id` bigint(20) DEFAULT NULL COMMENT '通知id',
  `receiver_id` bigint(20) DEFAULT NULL COMMENT '接收者id',
  `status` int(11) DEFAULT NULL COMMENT '消息状态(1.已读,2.未读)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table role
# ------------------------------------------------------------

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table role_acl
# ------------------------------------------------------------

CREATE TABLE `role_acl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `acl_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table score
# ------------------------------------------------------------

CREATE TABLE `score` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `semester` int(11) DEFAULT NULL COMMENT '学期数',
  `average_score` float DEFAULT NULL COMMENT '平均成绩',
  `achoevement_point` float DEFAULT NULL COMMENT '绩点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '学号或工号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `fix_phone` varchar(255) DEFAULT NULL COMMENT '固定电话',
  `id_card` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `mobile_phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码或者token',
  `qq_num` varchar(255) DEFAULT NULL COMMENT 'qq号',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别',
  `user_type` int(11) DEFAULT NULL COMMENT '用户类型：1.学生,2.教师,3教授,4.副教授',
  `institute` varchar(255) DEFAULT NULL COMMENT '学院',
  `major` varchar(255) DEFAULT NULL COMMENT '专业',
  `grade` int(11) DEFAULT NULL COMMENT '年级',
  `work_unit` varchar(255) DEFAULT NULL COMMENT '工作单位',
  `class_num` int(11) DEFAULT NULL COMMENT '班级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_project_group
# ------------------------------------------------------------

CREATE TABLE `user_project_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `technical_role` varchar(255) DEFAULT NULL COMMENT '技术职称',
  `work_division` varchar(800) DEFAULT NULL COMMENT '主要分工',
  `status` int(11) DEFAULT NULL COMMENT '参与状态:1.申请,已加入',
  `member_role` int(4) DEFAULT NULL COMMENT '成员身份：1.指导教师2.项目组长3.普通成员',
  `personal_judge` varchar(2000) DEFAULT NULL COMMENT '自我评价',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `join_time` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user_role
# ------------------------------------------------------------

CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
