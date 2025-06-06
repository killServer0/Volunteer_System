/*
SQLyog Ultimate v11.3 (64 bit)
MySQL - 5.7.32-log : Database - zhiyuanzheguanlixitong
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zhiyuanzheguanlixitong` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zhiyuanzheguanlixitong`;

/*Table structure for table `chat` */

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '提问志愿者',
  `chat_issue` varchar(200) DEFAULT NULL COMMENT '问题',
  `issue_time` timestamp NULL DEFAULT NULL COMMENT '问题时间 Search111',
  `chat_reply` varchar(200) DEFAULT NULL COMMENT '回复',
  `reply_time` timestamp NULL DEFAULT NULL COMMENT '回复时间 Search111',
  `zhuangtai_types` int(255) DEFAULT NULL COMMENT '状态',
  `chat_types` int(11) DEFAULT NULL COMMENT '数据类型',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客服聊天';

/*Data for the table `chat` */

insert  into `chat`(`id`,`zhiyuanzhe_id`,`chat_issue`,`issue_time`,`chat_reply`,`reply_time`,`zhuangtai_types`,`chat_types`,`insert_time`) values (1,1,'你好','2023-04-01 15:52:09',NULL,NULL,2,1,'2023-04-01 15:52:10'),(2,1,NULL,NULL,'16516','2023-04-01 15:56:53',NULL,2,'2023-04-01 15:56:54');

/*Table structure for table `config` */

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='配置文件';

/*Data for the table `config` */

insert  into `config`(`id`,`name`,`value`) values (1,'轮播图1','upload/config1.jpg'),(2,'轮播图2','upload/config2.jpg'),(3,'轮播图3','upload/config3.jpg');

/*Table structure for table `dictionary` */

DROP TABLE IF EXISTS `dictionary`;

CREATE TABLE `dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dic_code` varchar(200) DEFAULT NULL COMMENT '字段',
  `dic_name` varchar(200) DEFAULT NULL COMMENT '字段名',
  `code_index` int(11) DEFAULT NULL COMMENT '编码',
  `index_name` varchar(200) DEFAULT NULL COMMENT '编码名字  Search111 ',
  `super_id` int(11) DEFAULT NULL COMMENT '父字段id',
  `beizhu` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='字典';

/*Data for the table `dictionary` */

insert  into `dictionary`(`id`,`dic_code`,`dic_name`,`code_index`,`index_name`,`super_id`,`beizhu`,`create_time`) values (1,'sex_types','性别类型',1,'男',NULL,NULL,'2023-04-01 15:38:13'),(2,'sex_types','性别类型',2,'女',NULL,NULL,'2023-04-01 15:38:13'),(3,'gonggao_types','公告类型',1,'公告类型1',NULL,NULL,'2023-04-01 15:38:13'),(4,'gonggao_types','公告类型',2,'公告类型2',NULL,NULL,'2023-04-01 15:38:13'),(5,'forum_state_types','帖子状态',1,'发帖',NULL,NULL,'2023-04-01 15:38:13'),(6,'forum_state_types','帖子状态',2,'回帖',NULL,NULL,'2023-04-01 15:38:13'),(7,'zhihuodong_types','活动类型',1,'活动类型1',NULL,NULL,'2023-04-01 15:38:13'),(8,'zhihuodong_types','活动类型',2,'活动类型2',NULL,NULL,'2023-04-01 15:38:13'),(9,'zhihuodong_types','活动类型',3,'活动类型3',NULL,NULL,'2023-04-01 15:38:13'),(10,'zhihuodong_types','活动类型',4,'活动类型4',NULL,NULL,'2023-04-01 15:38:13'),(11,'zhihuodong_yuyue_yesno_types','报名状态',1,'待审核',NULL,NULL,'2023-04-01 15:38:13'),(12,'zhihuodong_yuyue_yesno_types','报名状态',2,'同意',NULL,NULL,'2023-04-01 15:38:13'),(13,'zhihuodong_yuyue_yesno_types','报名状态',3,'拒绝',NULL,NULL,'2023-04-01 15:38:14'),(14,'chat_types','数据类型',1,'问题',NULL,NULL,'2023-04-01 15:38:14'),(15,'chat_types','数据类型',2,'回复',NULL,NULL,'2023-04-01 15:38:14'),(16,'zhuangtai_types','状态',1,'未回复',NULL,NULL,'2023-04-01 15:38:14'),(17,'zhuangtai_types','状态',2,'已回复',NULL,NULL,'2023-04-01 15:38:14'),(18,'feihuodong_types','活动类型',1,'活动类型1',NULL,NULL,'2023-04-01 15:38:14'),(19,'feihuodong_types','活动类型',2,'活动类型2',NULL,NULL,'2023-04-01 15:38:14'),(20,'feihuodong_types','活动类型',3,'活动类型3',NULL,NULL,'2023-04-01 15:38:14'),(21,'feihuodong_types','活动类型',4,'活动类型4',NULL,NULL,'2023-04-01 15:38:14'),(22,'feihuodong_yuyue_yesno_types','报名状态',1,'待审核',NULL,NULL,'2023-04-01 15:38:14'),(23,'feihuodong_yuyue_yesno_types','报名状态',2,'同意',NULL,NULL,'2023-04-01 15:38:14'),(24,'feihuodong_yuyue_yesno_types','报名状态',3,'拒绝',NULL,NULL,'2023-04-01 15:38:14');

/*Table structure for table `feihuodong` */

DROP TABLE IF EXISTS `feihuodong`;

CREATE TABLE `feihuodong` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `feizhiyuan_id` int(11) DEFAULT NULL COMMENT '游客',
  `feihuodong_name` varchar(200) DEFAULT NULL COMMENT '活动名称  Search111 ',
  `feihuodong_uuid_number` varchar(200) DEFAULT NULL COMMENT '活动编号',
  `feihuodong_photo` varchar(200) DEFAULT NULL COMMENT '活动照片',
  `feihuodong_address` varchar(200) DEFAULT NULL COMMENT '活动地点',
  `feihuodong_types` int(11) DEFAULT NULL COMMENT '活动类型 Search111',
  `feihuodong_kucun_number` int(11) DEFAULT NULL COMMENT '活动人数',
  `feihuodong_tiaojian` varchar(200) DEFAULT NULL COMMENT '参加活动条件',
  `feihuodong_content` longtext COMMENT '活动介绍 ',
  `feihuodong_delete` int(11) DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间  show1 show2 photoShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='活动';

/*Data for the table `feihuodong` */

insert  into `feihuodong`(`id`,`feizhiyuan_id`,`feihuodong_name`,`feihuodong_uuid_number`,`feihuodong_photo`,`feihuodong_address`,`feihuodong_types`,`feihuodong_kucun_number`,`feihuodong_tiaojian`,`feihuodong_content`,`feihuodong_delete`,`insert_time`,`create_time`) values (1,1,'活动名称1','1680334740361','upload/feihuodong1.jpg','活动地点1',1,101,'参加活动条件1','活动介绍1',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,1,'活动名称2','1680334740334','upload/feihuodong2.jpg','活动地点2',2,102,'参加活动条件2','活动介绍2',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,1,'活动名称3','1680334740362','upload/feihuodong3.jpg','活动地点3',3,103,'参加活动条件3','活动介绍3',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,1,'活动名称4','1680334740374','upload/feihuodong4.jpg','活动地点4',3,104,'参加活动条件4','活动介绍4',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,1,'活动名称5','1680334740367','upload/feihuodong5.jpg','活动地点5',3,105,'参加活动条件5','活动介绍5',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,1,'活动名称6','1680334740351','upload/feihuodong6.jpg','活动地点6',1,106,'参加活动条件6','活动介绍6',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,3,'活动名称7','1680334740395','upload/feihuodong7.jpg','活动地点7',3,107,'参加活动条件7','活动介绍7',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,2,'活动名称8','1680334740373','upload/feihuodong8.jpg','活动地点8',3,108,'参加活动条件8','活动介绍8',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,3,'活动名称9','1680334740349','upload/feihuodong9.jpg','活动地点9',4,109,'参加活动条件9','活动介绍9',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,2,'活动名称10','1680334740379','upload/feihuodong10.jpg','活动地点10',4,1010,'参加活动条件10','活动介绍10',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,2,'活动名称11','1680334740343','upload/feihuodong11.jpg','活动地点11',1,1011,'参加活动条件11','活动介绍11',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,2,'活动名称12','1680334740370','upload/feihuodong12.jpg','活动地点12',2,1012,'参加活动条件12','活动介绍12',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,3,'活动名称13','1680334740323','upload/feihuodong13.jpg','活动地点13',3,1013,'参加活动条件13','活动介绍13',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,2,'活动名称14','1680334740355','upload/feihuodong14.jpg','活动地点14',2,1014,'参加活动条件14','活动介绍14',1,'2023-04-01 15:39:00','2023-04-01 15:39:00');

/*Table structure for table `feihuodong_yuyue` */

DROP TABLE IF EXISTS `feihuodong_yuyue`;

CREATE TABLE `feihuodong_yuyue` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `feihuodong_yuyue_uuid_number` varchar(200) DEFAULT NULL COMMENT '报名编号 Search111 ',
  `feihuodong_id` int(11) DEFAULT NULL COMMENT '活动',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '志愿者',
  `feihuodong_yuyue_text` longtext COMMENT '报名理由',
  `feihuodong_yuyue_yesno_types` int(11) DEFAULT NULL COMMENT '报名状态 Search111 ',
  `feihuodong_yuyue_yesno_text` longtext COMMENT '审核回复',
  `feihuodong_yuyue_shenhe_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '活动报名时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='活动报名';

/*Data for the table `feihuodong_yuyue` */

insert  into `feihuodong_yuyue`(`id`,`feihuodong_yuyue_uuid_number`,`feihuodong_id`,`zhiyuanzhe_id`,`feihuodong_yuyue_text`,`feihuodong_yuyue_yesno_types`,`feihuodong_yuyue_yesno_text`,`feihuodong_yuyue_shenhe_time`,`insert_time`,`create_time`) values (1,'1680334740313',1,2,'报名理由1',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,'1680334740320',2,1,'报名理由2',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,'1680334740342',3,1,'报名理由3',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,'1680334740384',4,3,'报名理由4',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,'1680334740329',5,1,'报名理由5',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,'1680334740331',6,1,'报名理由6',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,'1680334740331',7,3,'报名理由7',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,'1680334740335',8,2,'报名理由8',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,'1680334740377',9,3,'报名理由9',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,'1680334740357',10,2,'报名理由10',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,'1680334740332',11,3,'报名理由11',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,'1680334740362',12,2,'报名理由12',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,'1680334740316',13,2,'报名理由13',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,'1680334740312',14,3,'报名理由14',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(15,'1680335446182',14,1,'我要参加',1,NULL,NULL,'2023-04-01 15:50:46','2023-04-01 15:50:46'),(16,'1680335594768',6,1,'2156165',2,'时间地点注意事项联系方式','2023-04-01 15:53:41','2023-04-01 15:53:15','2023-04-01 15:53:15');

/*Table structure for table `feizhiyuan` */

DROP TABLE IF EXISTS `feizhiyuan`;

CREATE TABLE `feizhiyuan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) DEFAULT NULL COMMENT '账户',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `feizhiyuan_name` varchar(200) DEFAULT NULL COMMENT '游客姓名 Search111 ',
  `feizhiyuan_phone` varchar(200) DEFAULT NULL COMMENT '游客手机号',
  `feizhiyuan_id_number` varchar(200) DEFAULT NULL COMMENT '游客身份证号',
  `feizhiyuan_photo` varchar(200) DEFAULT NULL COMMENT '游客头像',
  `sex_types` int(11) DEFAULT NULL COMMENT '性别',
  `feizhiyuan_email` varchar(200) DEFAULT NULL COMMENT '游客邮箱',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='游客';

/*Data for the table `feizhiyuan` */

insert  into `feizhiyuan`(`id`,`username`,`password`,`feizhiyuan_name`,`feizhiyuan_phone`,`feizhiyuan_id_number`,`feizhiyuan_photo`,`sex_types`,`feizhiyuan_email`,`create_time`) values (1,'a1','123456','游客姓名1','17703786901','410224199010102001','upload/feizhiyuan1.jpg',1,'1@qq.com','2023-04-01 15:39:00'),(2,'a2','123456','游客姓名2','17703786902','410224199010102002','upload/feizhiyuan2.jpg',1,'2@qq.com','2023-04-01 15:39:00'),(3,'a3','123456','游客姓名3','17703786903','410224199010102003','upload/feizhiyuan3.jpg',2,'3@qq.com','2023-04-01 15:39:00');

/*Table structure for table `forum` */

DROP TABLE IF EXISTS `forum`;

CREATE TABLE `forum` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `forum_name` varchar(200) DEFAULT NULL COMMENT '帖子标题  Search111 ',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '志愿者',
  `feizhiyuan_id` int(11) DEFAULT NULL COMMENT '游客',
  `users_id` int(11) DEFAULT NULL COMMENT '管理员',
  `forum_content` longtext COMMENT '发布内容',
  `super_ids` int(11) DEFAULT NULL COMMENT '父id',
  `forum_state_types` int(11) DEFAULT NULL COMMENT '帖子状态',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '发帖时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='论坛';

/*Data for the table `forum` */

insert  into `forum`(`id`,`forum_name`,`zhiyuanzhe_id`,`feizhiyuan_id`,`users_id`,`forum_content`,`super_ids`,`forum_state_types`,`insert_time`,`update_time`,`create_time`) values (1,'帖子标题1',3,NULL,NULL,'发布内容1',299,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,'帖子标题2',1,NULL,NULL,'发布内容2',169,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,'帖子标题3',1,NULL,NULL,'发布内容3',214,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,'帖子标题4',2,NULL,NULL,'发布内容4',269,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,'帖子标题5',1,NULL,NULL,'发布内容5',143,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,'帖子标题6',2,NULL,NULL,'发布内容6',237,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,'帖子标题7',1,NULL,NULL,'发布内容7',82,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,'帖子标题8',2,NULL,NULL,'发布内容8',414,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,'帖子标题9',2,NULL,NULL,'发布内容9',488,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,'帖子标题10',3,NULL,NULL,'发布内容10',134,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,'帖子标题11',1,NULL,NULL,'发布内容11',143,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,'帖子标题12',1,NULL,NULL,'发布内容12',38,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,'帖子标题13',2,NULL,NULL,'发布内容13',431,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,'帖子标题14',2,NULL,NULL,'发布内容14',154,1,'2023-04-01 15:39:00','2023-04-01 15:39:00','2023-04-01 15:39:00'),(15,'帖子',1,NULL,NULL,'<p>帖子111</p>',NULL,1,'2023-04-01 15:51:04',NULL,'2023-04-01 15:51:04'),(16,NULL,NULL,1,NULL,'1261256',15,2,'2023-04-01 15:53:57',NULL,'2023-04-01 15:53:57');

/*Table structure for table `gonggao` */

DROP TABLE IF EXISTS `gonggao`;

CREATE TABLE `gonggao` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `gonggao_name` varchar(200) DEFAULT NULL COMMENT '公告名称 Search111  ',
  `gonggao_photo` varchar(200) DEFAULT NULL COMMENT '公告图片 ',
  `gonggao_types` int(11) NOT NULL COMMENT '公告类型 Search111 ',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '公告发布时间 ',
  `gonggao_content` longtext COMMENT '公告详情 ',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show1 show2 nameShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='公告';

/*Data for the table `gonggao` */

insert  into `gonggao`(`id`,`gonggao_name`,`gonggao_photo`,`gonggao_types`,`insert_time`,`gonggao_content`,`create_time`) values (1,'公告名称1','upload/gonggao1.jpg',2,'2023-04-01 15:39:00','公告详情1','2023-04-01 15:39:00'),(2,'公告名称2','upload/gonggao2.jpg',2,'2023-04-01 15:39:00','公告详情2','2023-04-01 15:39:00'),(3,'公告名称3','upload/gonggao3.jpg',1,'2023-04-01 15:39:00','公告详情3','2023-04-01 15:39:00'),(4,'公告名称4','upload/gonggao4.jpg',2,'2023-04-01 15:39:00','公告详情4','2023-04-01 15:39:00'),(5,'公告名称5','upload/gonggao5.jpg',2,'2023-04-01 15:39:00','公告详情5','2023-04-01 15:39:00'),(6,'公告名称6','upload/gonggao6.jpg',2,'2023-04-01 15:39:00','公告详情6','2023-04-01 15:39:00'),(7,'公告名称7','upload/gonggao7.jpg',1,'2023-04-01 15:39:00','公告详情7','2023-04-01 15:39:00'),(8,'公告名称8','upload/gonggao8.jpg',1,'2023-04-01 15:39:00','公告详情8','2023-04-01 15:39:00'),(9,'公告名称9','upload/gonggao9.jpg',1,'2023-04-01 15:39:00','公告详情9','2023-04-01 15:39:00'),(10,'公告名称10','upload/gonggao10.jpg',1,'2023-04-01 15:39:00','公告详情10','2023-04-01 15:39:00'),(11,'公告名称11','upload/gonggao11.jpg',2,'2023-04-01 15:39:00','公告详情11','2023-04-01 15:39:00'),(12,'公告名称12','upload/gonggao12.jpg',1,'2023-04-01 15:39:00','公告详情12','2023-04-01 15:39:00'),(13,'公告名称13','upload/gonggao13.jpg',2,'2023-04-01 15:39:00','公告详情13','2023-04-01 15:39:00'),(14,'公告名称14','upload/gonggao14.jpg',2,'2023-04-01 15:39:00','公告详情14','2023-04-01 15:39:00');

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序号',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `type` int(11) DEFAULT '0' COMMENT '类型 0:目录 1:菜单 2:按钮',
  `perms` varchar(500) DEFAULT NULL COMMENT '权限标识',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:正常 1:禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='菜单表';

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '儿童id',
  `username` varchar(100) NOT NULL COMMENT '儿童名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='token表';

/*Data for the table `token` */

insert  into `token`(`id`,`userid`,`username`,`tablename`,`role`,`token`,`addtime`,`expiratedtime`) values (1,1,'a1','zhiyuanzhe','志愿者','0t9ay2jb4kvut5bprhkbb1vt7ezj52s7','2023-04-01 15:40:34','2023-04-01 16:50:06'),(2,1,'admin','users','管理员','24tx5h0c2i2hma8bcwvzairc9uzynjjs','2023-04-01 15:44:43','2023-04-01 16:56:27'),(3,1,'a1','feizhiyuan','游客','g9kvlia9ck506d1m2ovml1wikha9ligx','2023-04-01 15:47:33','2023-04-01 16:52:21');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '儿童名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(100) DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员';

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`role`,`addtime`) values (1,'admin','admin','管理员','2023-04-01 15:38:13');

/*Table structure for table `zhihuodong` */

DROP TABLE IF EXISTS `zhihuodong`;

CREATE TABLE `zhihuodong` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '志愿者',
  `zhihuodong_name` varchar(200) DEFAULT NULL COMMENT '活动名称  Search111 ',
  `zhihuodong_uuid_number` varchar(200) DEFAULT NULL COMMENT '活动编号',
  `zhihuodong_photo` varchar(200) DEFAULT NULL COMMENT '活动照片',
  `zhihuodong_address` varchar(200) DEFAULT NULL COMMENT '活动地点',
  `zhihuodong_types` int(11) DEFAULT NULL COMMENT '活动类型 Search111',
  `zhihuodong_kucun_number` int(11) DEFAULT NULL COMMENT '活动人数',
  `zhihuodong_tiaojian` varchar(200) DEFAULT NULL COMMENT '参加活动条件',
  `zhihuodong_content` longtext COMMENT '活动介绍 ',
  `zhihuodong_delete` int(11) DEFAULT NULL COMMENT '逻辑删除',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '录入时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间  show1 show2 photoShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='志愿者活动';

/*Data for the table `zhihuodong` */

insert  into `zhihuodong`(`id`,`zhiyuanzhe_id`,`zhihuodong_name`,`zhihuodong_uuid_number`,`zhihuodong_photo`,`zhihuodong_address`,`zhihuodong_types`,`zhihuodong_kucun_number`,`zhihuodong_tiaojian`,`zhihuodong_content`,`zhihuodong_delete`,`insert_time`,`create_time`) values (1,1,'活动名称1','1680334740384','upload/zhihuodong1.jpg','活动地点1',4,101,'参加活动条件1','活动介绍1',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,2,'活动名称2','1680334740384','upload/zhihuodong2.jpg','活动地点2',4,102,'参加活动条件2','活动介绍2',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,2,'活动名称3','1680334740321','upload/zhihuodong3.jpg','活动地点3',2,103,'参加活动条件3','活动介绍3',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,1,'活动名称4','1680334740383','upload/zhihuodong4.jpg','活动地点4',2,104,'参加活动条件4','活动介绍4',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,1,'活动名称5','1680334740380','upload/zhihuodong5.jpg','活动地点5',3,105,'参加活动条件5','活动介绍5',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,2,'活动名称6','1680334740346','upload/zhihuodong6.jpg','活动地点6',2,106,'参加活动条件6','活动介绍6',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,3,'活动名称7','1680334740370','upload/zhihuodong7.jpg','活动地点7',4,107,'参加活动条件7','活动介绍7',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,1,'活动名称8','1680334740322','upload/zhihuodong8.jpg','活动地点8',1,108,'参加活动条件8','活动介绍8',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,3,'活动名称9','1680334740359','upload/zhihuodong9.jpg','活动地点9',2,109,'参加活动条件9','活动介绍9',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,3,'活动名称10','1680334740340','upload/zhihuodong10.jpg','活动地点10',1,1010,'参加活动条件10','活动介绍10',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,2,'活动名称11','1680334740321','upload/zhihuodong11.jpg','活动地点11',4,1011,'参加活动条件11','活动介绍11',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,2,'活动名称12','1680334740357','upload/zhihuodong12.jpg','活动地点12',3,1012,'参加活动条件12','活动介绍12',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,1,'活动名称13','1680334740376','upload/zhihuodong13.jpg','活动地点13',4,1013,'参加活动条件13','活动介绍13',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,1,'活动名称14','1680334740410','upload/zhihuodong14.jpg','活动地点14',1,1014,'参加活动条件14','活动介绍14',1,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(15,1,'丫丫','1680335504641','upload/1680335513164.jpg','sad',3,20,'10 ','sad大',1,'2023-04-01 15:52:01','2023-04-01 15:52:01');

/*Table structure for table `zhihuodong_yuyue` */

DROP TABLE IF EXISTS `zhihuodong_yuyue`;

CREATE TABLE `zhihuodong_yuyue` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `zhihuodong_yuyue_uuid_number` varchar(200) DEFAULT NULL COMMENT '报名编号 Search111 ',
  `zhihuodong_id` int(11) DEFAULT NULL COMMENT '活动',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '游客',
  `zhihuodong_yuyue_text` longtext COMMENT '报名理由',
  `zhihuodong_yuyue_yesno_types` int(11) DEFAULT NULL COMMENT '报名状态 Search111 ',
  `zhihuodong_yuyue_yesno_text` longtext COMMENT '审核回复',
  `zhihuodong_yuyue_shenhe_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '活动报名时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='志愿者活动报名';

/*Data for the table `zhihuodong_yuyue` */

insert  into `zhihuodong_yuyue`(`id`,`zhihuodong_yuyue_uuid_number`,`zhihuodong_id`,`zhiyuanzhe_id`,`zhihuodong_yuyue_text`,`zhihuodong_yuyue_yesno_types`,`zhihuodong_yuyue_yesno_text`,`zhihuodong_yuyue_shenhe_time`,`insert_time`,`create_time`) values (1,'1680334740325',1,2,'报名理由1',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,'1680334740378',2,2,'报名理由2',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,'1680334740351',3,3,'报名理由3',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,'1680334740343',4,1,'报名理由4',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,'1680334740336',5,1,'报名理由5',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,'1680334740400',6,2,'报名理由6',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,'1680334740365',7,1,'报名理由7',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,'1680334740377',8,3,'报名理由8',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,'1680334740411',9,1,'报名理由9',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,'1680334740383',10,1,'报名理由10',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,'1680334740411',11,3,'报名理由11',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,'1680334740368',12,1,'报名理由12',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,'1680334740358',13,2,'报名理由13',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,'1680334740369',14,1,'报名理由14',1,NULL,NULL,'2023-04-01 15:39:00','2023-04-01 15:39:00'),(15,'1680335481442',14,1,'想我参加吗',1,NULL,NULL,'2023-04-01 15:51:21','2023-04-01 15:51:21');

/*Table structure for table `zhiyuanzhe` */

DROP TABLE IF EXISTS `zhiyuanzhe`;

CREATE TABLE `zhiyuanzhe` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(200) DEFAULT NULL COMMENT '账户',
  `password` varchar(200) DEFAULT NULL COMMENT '密码',
  `zhiyuanzhe_name` varchar(200) DEFAULT NULL COMMENT '志愿者姓名 Search111 ',
  `zhiyuanzhe_phone` varchar(200) DEFAULT NULL COMMENT '志愿者手机号',
  `zhiyuanzhe_id_number` varchar(200) DEFAULT NULL COMMENT '志愿者身份证号',
  `zhiyuanzhe_photo` varchar(200) DEFAULT NULL COMMENT '志愿者头像',
  `sex_types` int(11) DEFAULT NULL COMMENT '性别',
  `zhiyuanzhe_email` varchar(200) DEFAULT NULL COMMENT '志愿者邮箱',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT 1 COMMENT '状态：1-正常，2-审核中，3-禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='志愿者';

/*Data for the table `zhiyuanzhe` */

insert  into `zhiyuanzhe`(`id`,`username`,`password`,`zhiyuanzhe_name`,`zhiyuanzhe_phone`,`zhiyuanzhe_id_number`,`zhiyuanzhe_photo`,`sex_types`,`zhiyuanzhe_email`,`create_time`,`status`) values (1,'a1','123456','志愿者姓名1','17703786901','410224199010102001','upload/zhiyuanzhe1.jpg',2,'1@qq.com','2023-04-01 15:39:00',1),(2,'a2','123456','志愿者姓名2','17703786902','410224199010102002','upload/zhiyuanzhe2.jpg',1,'2@qq.com','2023-04-01 15:39:00',1),(3,'a3','123456','志愿者姓名3','17703786903','410224199010102003','upload/zhiyuanzhe3.jpg',2,'3@qq.com','2023-04-01 15:39:00',1);

/*Table structure for table `zhiyuanzhe_liuyan` */

DROP TABLE IF EXISTS `zhiyuanzhe_liuyan`;

CREATE TABLE `zhiyuanzhe_liuyan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `feizhiyuan_id` int(11) DEFAULT NULL COMMENT '游客',
  `zhiyuanzhe_id` int(11) DEFAULT NULL COMMENT '志愿者',
  `zhiyuanzhe_liuyan_text` longtext COMMENT '留言内容',
  `insert_time` timestamp NULL DEFAULT NULL COMMENT '留言时间',
  `reply_text` longtext COMMENT '回复内容',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '回复时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 show3 listShow',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='志愿者留言';

/*Data for the table `zhiyuanzhe_liuyan` */

insert  into `zhiyuanzhe_liuyan`(`id`,`feizhiyuan_id`,`zhiyuanzhe_id`,`zhiyuanzhe_liuyan_text`,`insert_time`,`reply_text`,`update_time`,`create_time`) values (1,2,2,'留言内容1','2023-04-01 15:39:00','回复信息1','2023-04-01 15:39:00','2023-04-01 15:39:00'),(2,3,1,'留言内容2','2023-04-01 15:39:00','回复信息2','2023-04-01 15:39:00','2023-04-01 15:39:00'),(3,2,3,'留言内容3','2023-04-01 15:39:00','回复信息3','2023-04-01 15:39:00','2023-04-01 15:39:00'),(4,3,1,'留言内容4','2023-04-01 15:39:00','回复信息4','2023-04-01 15:39:00','2023-04-01 15:39:00'),(5,3,1,'留言内容5','2023-04-01 15:39:00','回复信息5','2023-04-01 15:39:00','2023-04-01 15:39:00'),(6,2,1,'留言内容6','2023-04-01 15:39:00','回复信息6','2023-04-01 15:39:00','2023-04-01 15:39:00'),(7,1,2,'留言内容7','2023-04-01 15:39:00','回复信息7','2023-04-01 15:39:00','2023-04-01 15:39:00'),(8,1,3,'留言内容8','2023-04-01 15:39:00','回复信息8','2023-04-01 15:39:00','2023-04-01 15:39:00'),(9,1,3,'留言内容9','2023-04-01 15:39:00','回复信息9','2023-04-01 15:39:00','2023-04-01 15:39:00'),(10,1,3,'留言内容10','2023-04-01 15:39:00','回复信息10','2023-04-01 15:39:00','2023-04-01 15:39:00'),(11,1,3,'留言内容11','2023-04-01 15:39:00','回复信息11','2023-04-01 15:39:00','2023-04-01 15:39:00'),(12,3,3,'留言内容12','2023-04-01 15:39:00','回复信息12','2023-04-01 15:39:00','2023-04-01 15:39:00'),(13,3,2,'留言内容13','2023-04-01 15:39:00','回复信息13','2023-04-01 15:39:00','2023-04-01 15:39:00'),(14,2,3,'留言内容14','2023-04-01 15:39:00','回复信息14','2023-04-01 15:39:00','2023-04-01 15:39:00'),(15,1,1,'倒计时爱打架撒','2023-04-01 15:54:56','',NULL,'2023-04-01 15:54:56');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- 更新所有表中的2023年为2025年
UPDATE `chat` SET 
    `issue_time` = DATE_ADD(`issue_time`, INTERVAL 2 YEAR),
    `reply_time` = DATE_ADD(`reply_time`, INTERVAL 2 YEAR),
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR)
WHERE YEAR(`issue_time`) = 2023 OR YEAR(`reply_time`) = 2023 OR YEAR(`insert_time`) = 2023;

UPDATE `dictionary` SET 
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`create_time`) = 2023;

UPDATE `feihuodong` SET 
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`insert_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `feihuodong_yuyue` SET 
    `feihuodong_yuyue_shenhe_time` = DATE_ADD(`feihuodong_yuyue_shenhe_time`, INTERVAL 2 YEAR),
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`feihuodong_yuyue_shenhe_time`) = 2023 OR YEAR(`insert_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `feizhiyuan` SET 
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`create_time`) = 2023;

UPDATE `forum` SET 
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `update_time` = DATE_ADD(`update_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`insert_time`) = 2023 OR YEAR(`update_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `gonggao` SET 
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`insert_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `token` SET 
    `addtime` = DATE_ADD(`addtime`, INTERVAL 2 YEAR),
    `expiratedtime` = DATE_ADD(`expiratedtime`, INTERVAL 2 YEAR)
WHERE YEAR(`addtime`) = 2023 OR YEAR(`expiratedtime`) = 2023;

UPDATE `users` SET 
    `addtime` = DATE_ADD(`addtime`, INTERVAL 2 YEAR)
WHERE YEAR(`addtime`) = 2023;

UPDATE `zhihuodong` SET 
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`insert_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `zhihuodong_yuyue` SET 
    `zhihuodong_yuyue_shenhe_time` = DATE_ADD(`zhihuodong_yuyue_shenhe_time`, INTERVAL 2 YEAR),
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`zhihuodong_yuyue_shenhe_time`) = 2023 OR YEAR(`insert_time`) = 2023 OR YEAR(`create_time`) = 2023;

UPDATE `zhiyuanzhe` SET 
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`create_time`) = 2023;

UPDATE `zhiyuanzhe_liuyan` SET 
    `insert_time` = DATE_ADD(`insert_time`, INTERVAL 2 YEAR),
    `update_time` = DATE_ADD(`update_time`, INTERVAL 2 YEAR),
    `create_time` = DATE_ADD(`create_time`, INTERVAL 2 YEAR)
WHERE YEAR(`insert_time`) = 2023 OR YEAR(`update_time`) = 2023 OR YEAR(`create_time`) = 2023;
