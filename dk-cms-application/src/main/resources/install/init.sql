# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.23)
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table dao_account
# ------------------------------------------------------------

CREATE TABLE `dao_account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '4-20个字符（汉字、字母、数字、下划线）',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '登录用户名',
  `enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1正常，2禁用',
  `reg_time` datetime DEFAULT NULL COMMENT '注册时间',
  `login_times` int(10) unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_at` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_ip` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '最后登录IP',
  `role` int(20) DEFAULT '1' COMMENT '1 前台用户 2 管理员',
  `realname` varchar(50) DEFAULT NULL COMMENT '真名',
  `telephone` char(20) DEFAULT NULL COMMENT '手机',
  `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
  `gender` varchar(10) DEFAULT NULL,
  `age` varchar(10) DEFAULT NULL,
  `country` varchar(60) DEFAULT NULL,
  `province` varchar(60) DEFAULT NULL,
  `city` varchar(60) DEFAULT NULL,
  `county` varchar(60) DEFAULT NULL,
  `logo` varchar(100) DEFAULT '',
  `store_id` int(10) DEFAULT NULL COMMENT '店长所在门店',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_ad_category
# ------------------------------------------------------------

CREATE TABLE `dao_ad_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_ad_position
# ------------------------------------------------------------

CREATE TABLE `dao_ad_position` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(11) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` varchar(400) DEFAULT NULL,
  `width` int(11) DEFAULT '100',
  `height` int(11) DEFAULT '100',
  `content` varchar(400) DEFAULT NULL,
  `is_valid` tinyint(1) DEFAULT '1',
  `content_invalid` varchar(500) DEFAULT NULL,
  `content_image` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_api_log
# ------------------------------------------------------------

CREATE TABLE `dao_api_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(32) DEFAULT NULL COMMENT 'Token',
  `created_at` datetime DEFAULT NULL,
  `api_name` varchar(20) DEFAULT '' COMMENT 'apiname',
  `parameters` varchar(200) DEFAULT NULL COMMENT '参数',
  `result` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_api_token
# ------------------------------------------------------------

CREATE TABLE `dao_api_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(32) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `expired_at` datetime DEFAULT NULL,
  `is_valid` tinyint(4) DEFAULT '1',
  `manager_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_article
# ------------------------------------------------------------

CREATE TABLE `dao_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permalink` varchar(100) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL,
  `publish_date` datetime DEFAULT NULL COMMENT '发布时间',
  `content` text,
  `summary` text COMMENT '摘要',
  `title_color` varchar(100) DEFAULT NULL,
  `click` int(11) DEFAULT '0' COMMENT '点击量',
  `is_recommend` tinyint(4) DEFAULT '0' COMMENT '推荐',
  `is_show` tinyint(4) DEFAULT '1' COMMENT '是否前台显示',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否进入回收站,是否是草稿',
  `keywords` varchar(200) DEFAULT NULL,
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面配图',
  `author` varchar(30) DEFAULT NULL COMMENT '作者',
  `source` varchar(30) DEFAULT NULL COMMENT '来源',
  `manager_id` int(11) DEFAULT '0' COMMENT '发布管理员id',
  `ext_json` text,
  `tags` varchar(200) DEFAULT NULL COMMENT '逗号分割的tagId',
  `sort` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `typeId` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table dao_category
# ------------------------------------------------------------

CREATE TABLE `dao_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT '0',
  `category_type` int(11) DEFAULT '-1' COMMENT '类型',
  `category_name` varchar(100) DEFAULT NULL,
  `permalink` varchar(50) DEFAULT NULL,
  `sort` int(11) DEFAULT '0',
  `keywords` varchar(400) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `seo_description` varchar(600) DEFAULT NULL,
  `cover_image` varchar(200) DEFAULT NULL COMMENT '封面图',
  `template_cate` varchar(100) DEFAULT NULL COMMENT '栏目模板',
  `template_list` varchar(100) DEFAULT NULL COMMENT '列表页面模板',
  `template_content` varchar(100) DEFAULT NULL COMMENT '内容页面模板',
  `enter_page` tinyint(4) DEFAULT '1' COMMENT '进入跳转',
  `tags` varchar(200) DEFAULT NULL COMMENT '竖线分割的 tagid',
  `content` text COMMENT '栏目主页内容',
  `ext_json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_category_copy
# ------------------------------------------------------------

CREATE TABLE `dao_category_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT '0',
  `category_type` int(11) DEFAULT '-1' COMMENT '类型',
  `category_name` varchar(100) DEFAULT NULL,
  `permalink` varchar(50) DEFAULT NULL,
  `sort` int(11) DEFAULT '0',
  `keywords` varchar(400) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `seo_description` varchar(600) DEFAULT NULL,
  `cover_image` varchar(200) DEFAULT NULL COMMENT '封面图',
  `template_cate` varchar(100) DEFAULT NULL COMMENT '栏目模板',
  `template_list` varchar(100) DEFAULT NULL COMMENT '列表页面模板',
  `template_content` varchar(100) DEFAULT NULL COMMENT '内容页面模板',
  `enter_page` tinyint(4) DEFAULT '1' COMMENT '进入跳转',
  `tags` varchar(200) DEFAULT NULL COMMENT '竖线分割的 tagid',
  `content` text COMMENT '栏目主页内容',
  `ext_json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_model
# ------------------------------------------------------------

CREATE TABLE `dao_model` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `model_name` varchar(500) DEFAULT NULL,
  `field_list` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_model_field
# ------------------------------------------------------------

CREATE TABLE `dao_model_field` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `model_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `field_name` varchar(20) DEFAULT NULL,
  `options` varchar(100) DEFAULT NULL,
  `default_value` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_role
# ------------------------------------------------------------

CREATE TABLE `dao_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL,
  `role_desc` varchar(30) DEFAULT NULL,
  `is_enable` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_setting
# ------------------------------------------------------------

CREATE TABLE `dao_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sgroup` varchar(255) DEFAULT NULL,
  `skey` varchar(255) DEFAULT NULL,
  `svalue` varchar(255) DEFAULT NULL,
  `sdefault` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_single_page
# ------------------------------------------------------------

CREATE TABLE `dao_single_page` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `permalink` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `template` varchar(100) DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT NULL,
  `seo_keywords` varchar(300) DEFAULT NULL,
  `seo_description` varchar(600) DEFAULT NULL,
  `is_delete` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `permalink` (`permalink`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table dao_tag
# ------------------------------------------------------------

CREATE TABLE `dao_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(60) DEFAULT NULL,
  `tag_type` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `dao_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `article_id` int(11) DEFAULT NULL,
  `publish_at` datetime DEFAULT NULL,
  `content` varchar(600) DEFAULT NULL,
  `is_show` tinyint(4) DEFAULT '0' COMMENT '1 true 0 false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

LOCK TABLES `dao_setting` WRITE;
/*!40000 ALTER TABLE `dao_setting` DISABLE KEYS */;


INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(1,'站点变量','siteHost','http://www.dkcms.cc','http://www.dkcms.cc','网站域名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(2,'站点变量','siteDescription','DKCMS-飞一般的建站速度','DKCMS-飞一般的建站速度','站点简介');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(3,'站点变量','siteWebroot','webroot','webroot','生成页面的保存路径');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(4,'站点变量','siteTitle','DKCMS-飞一般的建站速度','多客CMS(DKCMS)','站点标题');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(5,'站点变量','siteLogo',NULL,'/assets/image/logo.png','站点logo');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(6,'站点变量','siteSeoKeywords','DKCMS-飞一般的建站速度','DKCMS-飞一般的建站速度','SEO关键词');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(7,'站点变量','siteContact','17319911640 / biz@365d.ink','联系方式','联系方式');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(8,'readonly','template','dk-blog-basic','dk-blog-basic','当前使用的模板名称');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(9,'readonly','templateSiteHome','index.html','index.html','默认首页模板文件名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(10,'readonly','templateListPage','list.html','list.html','默认列表页模板文件名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(11,'readonly','templateContentPage','content.html','content.html','默认内容页模板文件名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(13,'readonly','templateCategoryPage','category.html','category.html','默认子栏目首页模板文件名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(14,'readonly','templateSinglePage','page.html','page.html','默认单页面模板文件名');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(17,'default','can_register','false','false','是否开放用户注册');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(18,'default','can_comment','false','false','是否允许用户评论');

INSERT INTO `dao_setting` (`id`, `sgroup`, `skey`, `svalue`, `sdefault`, `name`)
VALUES
	(19,'default','use_default_template','true','true','未设定模板文件时，使用默认模板文件名');

/*!40000 ALTER TABLE `dao_setting` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;