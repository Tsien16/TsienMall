-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tsien_mall
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `mall_cart`
--

DROP TABLE IF EXISTS `mall_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `product_id` int(11) DEFAULT NULL COMMENT '商品ID',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `checked` int(11) DEFAULT NULL COMMENT '是否选择,1=已勾选,0=未勾选',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8 COMMENT='购物车';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_cart`
--

LOCK TABLES `mall_cart` WRITE;
/*!40000 ALTER TABLE `mall_cart` DISABLE KEYS */;
INSERT INTO `mall_cart` (`id`, `user_id`, `product_id`, `quantity`, `checked`, `create_time`, `update_time`) VALUES (126,21,26,1,1,'2017-04-13 21:27:06','2017-04-13 21:27:06');
/*!40000 ALTER TABLE `mall_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_category`
--

DROP TABLE IF EXISTS `mall_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类别ID\n当ID=0时说明是根节点,一级类别',
  `name` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '类别状态1-正常,2-已废弃',
  `sort_order` int(4) DEFAULT NULL COMMENT '排序编号,同类展示顺序,数值相等则自然排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100034 DEFAULT CHARSET=utf8 COMMENT='类别';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_category`
--

LOCK TABLES `mall_category` WRITE;
/*!40000 ALTER TABLE `mall_category` DISABLE KEYS */;
INSERT INTO `mall_category` (`id`, `parent_id`, `name`, `status`, `sort_order`, `create_time`, `update_time`) VALUES (100001,0,'家用电器',1,NULL,'2017-03-25 16:46:00','2017-03-25 16:46:00'),(100002,0,'数码3C',1,NULL,'2017-03-25 16:46:21','2017-03-25 16:46:21'),(100003,0,'服装箱包',1,NULL,'2017-03-25 16:49:53','2017-03-25 16:49:53'),(100004,0,'食品生鲜',1,NULL,'2017-03-25 16:50:19','2017-03-25 16:50:19'),(100005,0,'酒水饮料',1,NULL,'2017-03-25 16:50:29','2017-03-25 16:50:29'),(100006,100001,'冰箱',1,NULL,'2017-03-25 16:52:15','2017-03-25 16:52:15'),(100007,100001,'电视',1,NULL,'2017-03-25 16:52:26','2017-03-25 16:52:26'),(100008,100001,'洗衣机',1,NULL,'2017-03-25 16:52:39','2017-03-25 16:52:39'),(100009,100001,'空调',1,NULL,'2017-03-25 16:52:45','2017-03-25 16:52:45'),(100010,100001,'电热水器',1,NULL,'2017-03-25 16:52:54','2017-03-25 16:52:54'),(100011,100002,'电脑',1,NULL,'2017-03-25 16:53:18','2017-03-25 16:53:18'),(100012,100002,'手机',1,NULL,'2017-03-25 16:53:27','2017-03-25 16:53:27'),(100013,100002,'平板电脑',1,NULL,'2017-03-25 16:53:35','2017-03-25 16:53:35'),(100014,100002,'数码相机',1,NULL,'2017-03-25 16:53:56','2017-03-25 16:53:56'),(100015,100002,'3C配件',1,NULL,'2017-03-25 16:54:07','2017-03-25 16:54:07'),(100016,100003,'女装',1,NULL,'2017-03-25 16:54:44','2017-03-25 16:54:44'),(100017,100003,'帽子',1,NULL,'2017-03-25 16:54:51','2017-03-25 16:54:51'),(100018,100003,'旅行箱',1,NULL,'2017-03-25 16:55:02','2017-03-25 16:55:02'),(100019,100003,'手提包',1,NULL,'2017-03-25 16:55:09','2017-03-25 16:55:09'),(100020,100003,'保暖内衣',1,NULL,'2017-03-25 16:55:18','2017-03-25 16:55:18'),(100021,100004,'零食',1,NULL,'2017-03-25 16:55:30','2017-03-25 16:55:30'),(100022,100004,'生鲜',1,NULL,'2017-03-25 16:55:37','2017-03-25 16:55:37'),(100023,100004,'半成品菜',1,NULL,'2017-03-25 16:55:47','2017-03-25 16:55:47'),(100024,100004,'速冻食品',1,NULL,'2017-03-25 16:55:56','2017-03-25 16:55:56'),(100025,100004,'进口食品',1,NULL,'2017-03-25 16:56:06','2017-03-25 16:56:06'),(100026,100005,'白酒',1,NULL,'2017-03-25 16:56:22','2017-03-25 16:56:22'),(100027,100005,'红酒',1,NULL,'2017-03-25 16:56:30','2017-03-25 16:56:30'),(100028,100005,'饮料',1,NULL,'2017-03-25 16:56:37','2017-03-25 16:56:37'),(100029,100005,'调制鸡尾酒',1,NULL,'2017-03-25 16:56:45','2017-03-25 16:56:45'),(100030,100005,'进口洋酒',1,NULL,'2017-03-25 16:57:05','2017-03-25 16:57:05'),(100032,0,'已经修改的的家用电器子分类',1,NULL,'2019-02-18 00:33:31','2019-02-18 00:33:31'),(100033,0,'新增的家用电器子分类',1,NULL,'2019-02-18 15:21:32','2019-02-18 15:21:32');
/*!40000 ALTER TABLE `mall_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_order`
--

DROP TABLE IF EXISTS `mall_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `shipping_id` int(11) DEFAULT NULL COMMENT '收货地址ID',
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实际付款金额,单位是元,保留两位小数',
  `payment_type` int(4) DEFAULT NULL COMMENT '支付类型,1-在线支付',
  `postage` int(10) DEFAULT NULL COMMENT '运费,单位是元',
  `status` int(10) DEFAULT NULL COMMENT '订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8 COMMENT='订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_order`
--

LOCK TABLES `mall_order` WRITE;
/*!40000 ALTER TABLE `mall_order` DISABLE KEYS */;
INSERT INTO `mall_order` (`id`, `order_no`, `user_id`, `shipping_id`, `payment`, `payment_type`, `postage`, `status`, `payment_time`, `send_time`, `end_time`, `close_time`, `create_time`, `update_time`) VALUES (103,1491753014256,1,25,13998.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-09 23:50:14','2017-04-09 23:50:14'),(104,1491830695216,1,26,13998.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-10 21:24:55','2017-04-10 21:24:55'),(105,1492089528889,1,29,3299.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:18:48','2017-04-13 21:18:48'),(106,1492090946105,1,29,27894.00,1,0,20,'2017-04-13 21:42:40',NULL,NULL,NULL,'2017-04-13 21:42:26','2017-04-13 21:42:41'),(107,1492091003128,1,29,8597.00,1,0,20,'2017-04-13 21:43:38',NULL,NULL,NULL,'2017-04-13 21:43:23','2017-04-13 21:43:38'),(108,1492091051313,1,29,1999.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:11','2017-04-13 21:44:11'),(109,1492091061513,1,29,6598.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:21','2017-04-13 21:44:21'),(110,1492091069563,1,29,3299.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:29','2017-04-13 21:44:29'),(111,1492091076073,1,29,4299.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:36','2017-04-13 21:44:36'),(112,1492091083720,1,29,3299.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:43','2017-04-13 21:44:43'),(113,1492091089794,1,29,6999.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:49','2017-04-13 21:44:49'),(114,1492091096400,1,29,6598.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:44:56','2017-04-13 21:44:56'),(115,1492091102371,1,29,3299.00,1,0,10,NULL,NULL,NULL,NULL,'2017-04-13 21:45:02','2017-04-13 21:45:02'),(116,1492091110004,1,29,8598.00,1,0,40,'2017-04-13 21:55:16','2017-04-13 21:55:31',NULL,NULL,'2017-04-13 21:45:09','2017-04-13 21:55:31'),(117,1492091141269,1,29,22894.00,1,0,40,'2017-04-13 21:46:06','2019-06-17 16:49:35',NULL,NULL,'2017-04-13 21:45:41','2019-06-18 00:49:34'),(118,1560789588937,22,29,19990.00,1,0,0,NULL,NULL,NULL,NULL,'2019-06-18 00:39:48','2019-06-18 00:39:48'),(119,1560791433920,22,29,100000.00,1,0,10,NULL,NULL,NULL,NULL,'2019-06-18 01:10:33','2019-06-18 01:10:33');
/*!40000 ALTER TABLE `mall_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_order_item`
--

DROP TABLE IF EXISTS `mall_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单子表ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `product_id` int(11) DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片地址',
  `current_unit_price` decimal(20,2) DEFAULT NULL COMMENT '生成订单时的商品单价，单位是元,保留两位小数',
  `quantity` int(10) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '商品总价,单位是元,保留两位小数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`) USING BTREE,
  KEY `order_no_user_id_index` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COMMENT='订单详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_order_item`
--

LOCK TABLES `mall_order_item` WRITE;
/*!40000 ALTER TABLE `mall_order_item` DISABLE KEYS */;
INSERT INTO `mall_order_item` (`id`, `user_id`, `order_no`, `product_id`, `product_name`, `product_image`, `current_unit_price`, `quantity`, `total_price`, `create_time`, `update_time`) VALUES (113,1,1491753014256,26,'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机','241997c4-9e62-4824-b7f0-7425c3c28917.jpeg',6999.00,2,13998.00,'2017-04-09 23:50:14','2017-04-09 23:50:14'),(114,1,1491830695216,26,'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机','241997c4-9e62-4824-b7f0-7425c3c28917.jpeg',6999.00,2,13998.00,'2017-04-10 21:24:55','2017-04-10 21:24:55'),(115,1,1492089528889,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:18:48','2017-04-13 21:18:48'),(116,1,1492090946105,29,'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg',4299.00,2,8598.00,'2017-04-13 21:42:26','2017-04-13 21:42:26'),(117,1,1492090946105,28,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg',1999.00,1,1999.00,'2017-04-13 21:42:26','2017-04-13 21:42:26'),(118,1,1492090946105,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:42:26','2017-04-13 21:42:26'),(119,1,1492090946105,26,'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机','241997c4-9e62-4824-b7f0-7425c3c28917.jpeg',6999.00,2,13998.00,'2017-04-13 21:42:26','2017-04-13 21:42:26'),(120,1,1492091003128,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,2,6598.00,'2017-04-13 21:43:23','2017-04-13 21:43:23'),(121,1,1492091003128,28,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg',1999.00,1,1999.00,'2017-04-13 21:43:23','2017-04-13 21:43:23'),(122,1,1492091051313,28,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg',1999.00,1,1999.00,'2017-04-13 21:44:11','2017-04-13 21:44:11'),(123,1,1492091061513,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,2,6598.00,'2017-04-13 21:44:21','2017-04-13 21:44:21'),(124,1,1492091069563,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:44:29','2017-04-13 21:44:29'),(125,1,1492091076073,29,'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg',4299.00,1,4299.00,'2017-04-13 21:44:36','2017-04-13 21:44:36'),(126,1,1492091083720,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:44:43','2017-04-13 21:44:43'),(127,1,1492091089794,26,'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机','241997c4-9e62-4824-b7f0-7425c3c28917.jpeg',6999.00,1,6999.00,'2017-04-13 21:44:49','2017-04-13 21:44:49'),(128,1,1492091096400,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,2,6598.00,'2017-04-13 21:44:56','2017-04-13 21:44:56'),(129,1,1492091102371,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:45:02','2017-04-13 21:45:02'),(130,1,1492091110004,29,'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg',4299.00,2,8598.00,'2017-04-13 21:45:09','2017-04-13 21:45:09'),(131,1,1492091141269,26,'Apple iPhone 7 Plus (A1661) 128G 玫瑰金色 移动联通电信4G手机','241997c4-9e62-4824-b7f0-7425c3c28917.jpeg',6999.00,1,6999.00,'2017-04-13 21:45:41','2017-04-13 21:45:41'),(132,1,1492091141269,27,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg',3299.00,1,3299.00,'2017-04-13 21:45:41','2017-04-13 21:45:41'),(133,1,1492091141269,29,'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg',4299.00,2,8598.00,'2017-04-13 21:45:41','2017-04-13 21:45:41'),(134,1,1492091141269,28,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg',1999.00,2,3998.00,'2017-04-13 21:45:41','2017-04-13 21:45:41'),(135,22,1560789588937,28,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg',1999.00,10,19990.00,'2019-06-18 00:39:49','2019-06-18 00:39:49'),(136,22,1560791433920,26,'三星洗衣机','test.jpg',1000.00,100,100000.00,'2019-06-18 01:10:33','2019-06-18 01:10:33');
/*!40000 ALTER TABLE `mall_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_pay_info`
--

DROP TABLE IF EXISTS `mall_pay_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_pay_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付信息ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `pay_platform` int(10) DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信',
  `platform_number` varchar(200) DEFAULT NULL COMMENT '支付宝支付流水号',
  `platform_status` varchar(20) DEFAULT NULL COMMENT '支付宝支付状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='支付信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_pay_info`
--

LOCK TABLES `mall_pay_info` WRITE;
/*!40000 ALTER TABLE `mall_pay_info` DISABLE KEYS */;
INSERT INTO `mall_pay_info` (`id`, `user_id`, `order_no`, `pay_platform`, `platform_number`, `platform_status`, `create_time`, `update_time`) VALUES (53,1,1492090946105,1,'2017041321001004300200116250','WAIT_BUYER_PAY','2017-04-13 21:42:33','2017-04-13 21:42:33'),(54,1,1492090946105,1,'2017041321001004300200116250','TRADE_SUCCESS','2017-04-13 21:42:41','2017-04-13 21:42:41'),(55,1,1492091003128,1,'2017041321001004300200116251','WAIT_BUYER_PAY','2017-04-13 21:43:31','2017-04-13 21:43:31'),(56,1,1492091003128,1,'2017041321001004300200116251','TRADE_SUCCESS','2017-04-13 21:43:38','2017-04-13 21:43:38'),(57,1,1492091141269,1,'2017041321001004300200116252','WAIT_BUYER_PAY','2017-04-13 21:45:59','2017-04-13 21:45:59'),(58,1,1492091141269,1,'2017041321001004300200116252','TRADE_SUCCESS','2017-04-13 21:46:07','2017-04-13 21:46:07'),(59,1,1492091110004,1,'2017041321001004300200116396','WAIT_BUYER_PAY','2017-04-13 21:55:08','2017-04-13 21:55:08'),(60,1,1492091110004,1,'2017041321001004300200116396','TRADE_SUCCESS','2017-04-13 21:55:17','2017-04-13 21:55:17');
/*!40000 ALTER TABLE `mall_pay_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_product`
--

DROP TABLE IF EXISTS `mall_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID,对应mall_category表的主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图,url相对地址',
  `sub_images` text COMMENT '图片地址,json格式,扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格,单位-元保留两位小数',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` int(6) DEFAULT '1' COMMENT '商品状态.1-在售 2-下架 3-删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='商品';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_product`
--

LOCK TABLES `mall_product` WRITE;
/*!40000 ALTER TABLE `mall_product` DISABLE KEYS */;
INSERT INTO `mall_product` (`id`, `category_id`, `name`, `subtitle`, `main_image`, `sub_images`, `detail`, `price`, `stock`, `status`, `create_time`, `update_time`) VALUES (26,1,'三星洗衣机','三星大促销','test.jpg','test.jpg','detailtext',1000.00,0,1,NULL,'2019-06-18 01:10:33'),(27,100006,'Midea/美的 BCD-535WKZM(E)冰箱双开门对开门风冷无霜智能电家用','送品牌烤箱，五一大促','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg','ac3e571d-13ce-4fad-89e8-c92c2eccf536.jpeg,4bb02f1c-62d5-48cc-b358-97b05af5740d.jpeg,36bdb49c-72ae-4185-9297-78829b54b566.jpeg','<p><img alt=\"miaoshu.jpg\" src=\"http://img.happymmall.com/9c5c74e6-6615-4aa0-b1fc-c17a1eff6027.jpg\" width=\"790\" height=\"444\"><br></p><p><img alt=\"miaoshu2.jpg\" src=\"http://img.happymmall.com/31dc1a94-f354-48b8-a170-1a1a6de8751b.jpg\" width=\"790\" height=\"1441\"><img alt=\"miaoshu3.jpg\" src=\"http://img.happymmall.com/7862594b-3063-4b52-b7d4-cea980c604e0.jpg\" width=\"790\" height=\"1442\"><img alt=\"miaoshu4.jpg\" src=\"http://img.happymmall.com/9a650563-dc85-44d6-b174-d6960cfb1d6a.jpg\" width=\"790\" height=\"1441\"><br></p>',3299.00,8876,2,'2017-04-13 18:51:54','2017-04-13 21:45:41'),(28,100012,'4+64G送手环/Huawei/华为 nova 手机P9/P10plus青春','NOVA青春版1999元','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg','0093f5d3-bdb4-4fb0-bec5-5465dfd26363.jpeg,13da2172-4445-4eb5-a13f-c5d4ede8458c.jpeg,58d5d4b7-58d4-4948-81b6-2bae4f79bf02.jpeg','<p><img alt=\"11TB2fKK3cl0kpuFjSsziXXa.oVXa_!!1777180618.jpg\" src=\"http://img.happymmall.com/5c2d1c6d-9e09-48ce-bbdb-e833b42ff664.jpg\" width=\"790\" height=\"966\"><img alt=\"22TB2YP3AkEhnpuFjSZFpXXcpuXXa_!!1777180618.jpg\" src=\"http://img.happymmall.com/9a10b877-818f-4a27-b6f7-62887f3fb39d.jpg\" width=\"790\" height=\"1344\"><img alt=\"33TB2Yyshk.hnpuFjSZFpXXcpuXXa_!!1777180618.jpg\" src=\"http://img.happymmall.com/7d7fbd69-a3cb-4efe-8765-423bf8276e3e.jpg\" width=\"790\" height=\"700\"><img alt=\"TB2diyziB8kpuFjSspeXXc7IpXa_!!1777180618.jpg\" src=\"http://img.happymmall.com/1d7160d2-9dba-422f-b2a0-e92847ba6ce9.jpg\" width=\"790\" height=\"393\"><br></p>',1999.00,9984,1,'2017-04-13 18:57:18','2019-06-18 00:39:49'),(29,100008,'Haier/海尔HJ100-1HU1 10公斤滚筒洗衣机全自动带烘干家用大容量 洗烘一体','门店机型 德邦送货','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg','173335a4-5dce-4afd-9f18-a10623724c4e.jpeg,42b1b8bc-27c7-4ee1-80ab-753d216a1d49.jpeg,2f1b3de1-1eb1-4c18-8ca2-518934931bec.jpeg','<p><img alt=\"1TB2WLZrcIaK.eBjSspjXXXL.XXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/ffcce953-81bd-463c-acd1-d690b263d6df.jpg\" width=\"790\" height=\"920\"><img alt=\"2TB2zhOFbZCO.eBjSZFzXXaRiVXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/58a7bd25-c3e7-4248-9dba-158ef2a90e70.jpg\" width=\"790\" height=\"1052\"><img alt=\"3TB27mCtb7WM.eBjSZFhXXbdWpXa_!!2114960396.jpg\" src=\"http://img.happymmall.com/2edbe9b3-28be-4a8b-a9c3-82e40703f22f.jpg\" width=\"790\" height=\"820\"><br></p>',4299.00,9993,1,'2017-04-13 19:07:47','2017-04-13 21:45:41'),(30,1,'三星洗衣机','三星大促销','test.jpg','test.jpg','detailtext',1000.00,100,1,'2019-04-21 01:38:35','2019-04-21 01:38:35');
/*!40000 ALTER TABLE `mall_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_shipping`
--

DROP TABLE IF EXISTS `mall_shipping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_shipping` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收货地址ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `receiver_name` varchar(20) DEFAULT NULL COMMENT '收货姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货固定电话',
  `receiver_mobile` varchar(20) DEFAULT NULL COMMENT '收货移动电话',
  `receiver_province` varchar(20) DEFAULT NULL COMMENT '省份',
  `receiver_city` varchar(20) DEFAULT NULL COMMENT '城市',
  `receiver_district` varchar(20) DEFAULT NULL COMMENT '区/县',
  `receiver_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `receiver_zip` varchar(6) DEFAULT NULL COMMENT '邮编',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='收货地址';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_shipping`
--

LOCK TABLES `mall_shipping` WRITE;
/*!40000 ALTER TABLE `mall_shipping` DISABLE KEYS */;
INSERT INTO `mall_shipping` (`id`, `user_id`, `receiver_name`, `receiver_phone`, `receiver_mobile`, `receiver_province`, `receiver_city`, `receiver_district`, `receiver_address`, `receiver_zip`, `create_time`, `update_time`) VALUES (4,13,'geely','010','18688888888','北京','北京市','海淀区','中关村','100000','2017-01-22 14:26:25','2017-01-22 14:26:25'),(7,17,'Rosen','13800138000','13800138000','北京','北京',NULL,'中关村','100000','2017-03-29 12:11:01','2017-03-29 12:11:01'),(29,22,'devinc','010','18688888888','北京','北京市',NULL,'中关村','100000',NULL,'2019-04-21 22:29:48'),(34,22,'geely','010','18688888888','北京','北京市',NULL,'中关村','100000','2019-04-21 22:25:02','2019-04-21 22:25:02');
/*!40000 ALTER TABLE `mall_shipping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mall_user`
--

DROP TABLE IF EXISTS `mall_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mall_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '用户密码，MD5加密',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `question` varchar(100) DEFAULT NULL COMMENT '找回密码问题',
  `answer` varchar(100) DEFAULT NULL COMMENT '找回密码答案',
  `role` int(4) NOT NULL COMMENT '角色0-管理员,1-普通用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mall_user`
--

LOCK TABLES `mall_user` WRITE;
/*!40000 ALTER TABLE `mall_user` DISABLE KEYS */;
INSERT INTO `mall_user` (`id`, `username`, `password`, `email`, `phone`, `question`, `answer`, `role`, `create_time`, `update_time`) VALUES (1,'admin','427338237BD929443EC5D48E24FD2B1A','admin@happymmall.com','13800138000','问题','答案',1,'2016-11-06 16:56:45','2017-04-04 19:27:36'),(13,'geely','08E9A6EA287E70E7E3F7C982BF7923AC','geely@happymmall.com','13800138000','问题','答案',0,'2016-11-19 22:19:25','2016-11-19 22:19:25'),(17,'rosen','095AC193FE2212EEC7A93E8FEFF11902','rosen1@happymmall.com','13800138000','问题','答案',0,'2017-03-17 10:51:33','2017-04-09 23:13:26'),(21,'soonerbetter','DE6D76FE7C40D5A1A8F04213F2BEFBEE','test06@happymmall.com','13800138000','105204','105204',0,'2017-04-13 21:26:22','2017-04-13 21:26:22'),(22,'tsien','F524F10B4773F0E3DDF6A620E9AD1CB4','tsien16@qq.com','15210931626','who is your daddy？','Tsien',1,'2019-02-12 18:41:18','2019-06-22 21:12:09');
/*!40000 ALTER TABLE `mall_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-25 10:17:49
