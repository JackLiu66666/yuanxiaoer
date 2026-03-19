-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: yx2_demo
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `agency`
--

DROP TABLE IF EXISTS `agency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agency` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `username` varchar(50) NOT NULL COMMENT '唯一用户名',
  `password` varchar(100) NOT NULL COMMENT '加密密码',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态 0-未审核 1-待审核 2-通过 3-不通过',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agency_audit_application`
--

DROP TABLE IF EXISTS `agency_audit_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agency_audit_application` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `apply_id` varchar(64) NOT NULL COMMENT '申请唯一标识（自定义唯一ID）',
  `agency_id` bigint NOT NULL COMMENT '关联代理商表ID',
  `agency_full_name` varchar(200) NOT NULL COMMENT '机构全称',
  `social_credit_code` varchar(18) NOT NULL COMMENT '统一社会信用代码',
  `registered_address` varchar(500) NOT NULL COMMENT '注册地址',
  `agency_type` varchar(50) NOT NULL COMMENT '机构类型：保险代理/保险经纪/渠道代理/其他',
  `business_license_no` varchar(100) NOT NULL COMMENT '经营许可证/备案号',
  `license_valid_start` date NOT NULL COMMENT '许可证有效期-开始',
  `license_valid_end` date NOT NULL COMMENT '许可证有效期-结束',
  `legal_person_name` varchar(50) NOT NULL COMMENT '法定代表人姓名',
  `legal_person_id_card` varchar(18) NOT NULL COMMENT '法定代表人身份证号',
  `legal_person_phone` varchar(20) NOT NULL COMMENT '法定代表人手机号',
  `contact_person_name` varchar(50) NOT NULL COMMENT '对接负责人姓名',
  `contact_person_position` varchar(50) NOT NULL COMMENT '对接负责人职务',
  `contact_person_phone` varchar(20) NOT NULL COMMENT '对接负责人手机号',
  `contact_person_office_phone` varchar(20) DEFAULT NULL COMMENT '对接负责人办公电话',
  `contact_person_email` varchar(100) NOT NULL COMMENT '对接负责人邮箱',
  `company_fixed_phone` varchar(20) NOT NULL COMMENT '公司固定电话',
  `business_scope` json DEFAULT NULL COMMENT '合作业务范围 JSON 数组',
  `cooperation_area` json DEFAULT NULL COMMENT '合作区域 JSON 数组',
  `cooperation_mode` varchar(100) NOT NULL COMMENT '合作模式',
  `authenticity_commitment` tinyint NOT NULL COMMENT '资料真实性承诺：1-同意',
  `seal_file_path` varchar(500) NOT NULL COMMENT '申请单位盖章文件路径',
  `apply_date` date NOT NULL COMMENT '申请日期',
  `is_deleted` tinyint DEFAULT '0' COMMENT '软删除：0-未删 1-已删',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `audit_status` int NOT NULL DEFAULT '0' COMMENT '审核状态：0-未审核，1-待审核，2-审核通过，3-审核不通过',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_id` (`apply_id`),
  KEY `idx_agency_id` (`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理商合作审核申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agency_business_scope_relation`
--

DROP TABLE IF EXISTS `agency_business_scope_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agency_business_scope_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agency_id` bigint NOT NULL COMMENT '机构ID，关联agency表的id',
  `business_scope` tinyint NOT NULL COMMENT '业务范围（1-6）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agency_business` (`agency_id`,`business_scope`),
  KEY `idx_agency_id` (`agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='机构业务范围关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `agency_info`
--

DROP TABLE IF EXISTS `agency_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agency_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `agency_id` bigint NOT NULL COMMENT '关联代理商 ID（与 agency 表的 id 关联）',
  `agency_full_name` varchar(200) NOT NULL COMMENT '机构全称',
  `social_credit_code` varchar(100) NOT NULL COMMENT '统一社会信用代码',
  `registered_address` varchar(500) NOT NULL COMMENT '注册地址',
  `agency_type` varchar(50) NOT NULL COMMENT '机构类型',
  `business_license_no` varchar(100) NOT NULL COMMENT '经营许可证/备案号',
  `license_valid_start` date DEFAULT NULL COMMENT '许可证有效期开始',
  `license_valid_end` date DEFAULT NULL COMMENT '许可证有效期结束',
  `legal_person_name` varchar(100) NOT NULL COMMENT '法定代表人姓名',
  `legal_person_id_card` varchar(100) NOT NULL COMMENT '法定代表人身份证号',
  `legal_person_phone` varchar(20) NOT NULL COMMENT '法定代表人手机号',
  `contact_person_name` varchar(100) NOT NULL COMMENT '对接负责人姓名',
  `contact_person_position` varchar(50) DEFAULT NULL COMMENT '对接负责人职务',
  `contact_person_phone` varchar(20) NOT NULL COMMENT '对接负责人手机号',
  `contact_person_office_phone` varchar(20) DEFAULT NULL COMMENT '对接负责人办公电话',
  `contact_person_email` varchar(100) DEFAULT NULL COMMENT '对接负责人邮箱',
  `company_fixed_phone` varchar(20) DEFAULT NULL COMMENT '公司固定电话',
  `business_scope` json NOT NULL DEFAULT (json_array()) COMMENT '合作业务范围（JSON 数组）',
  `cooperation_area` json NOT NULL DEFAULT (json_array()) COMMENT '合作区域（JSON 数组）',
  `cooperation_mode` varchar(50) DEFAULT NULL COMMENT '合作模式',
  `authenticity_commitment` int NOT NULL DEFAULT '1' COMMENT '资料真实性承诺（1-同意）',
  `seal_file_path` varchar(500) DEFAULT NULL COMMENT '盖章文件路径（相对路径）',
  `apply_date` date DEFAULT NULL COMMENT '申请日期',
  `audit_status` int NOT NULL DEFAULT '2' COMMENT '审核状态：2-审核通过',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agency_id` (`agency_id`) COMMENT '确保每个代理商只有一条信息记录',
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理商信息表（审核通过后存储）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hospital`
--

DROP TABLE IF EXISTS `hospital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hospital` (
  `id` int NOT NULL COMMENT 'id',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district_county` varchar(50) DEFAULT NULL COMMENT '区县',
  `hospital_name` varchar(100) DEFAULT NULL COMMENT '医院名称',
  `hospital_level` varchar(20) DEFAULT NULL COMMENT '医院等级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全国二级以上医院信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hospital_agency_relation`
--

DROP TABLE IF EXISTS `hospital_agency_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hospital_agency_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联记录ID（主键）',
  `hospital_id` int NOT NULL COMMENT '关联的医院ID（外键）',
  `agency_id` bigint NOT NULL COMMENT '关联的代理商ID（外键）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hospital_agency` (`hospital_id`,`agency_id`),
  KEY `agency_id` (`agency_id`),
  CONSTRAINT `hospital_agency_relation_ibfk_1` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `hospital_agency_relation_ibfk_2` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='医院-代理商关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `insurance_company`
--

DROP TABLE IF EXISTS `insurance_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insurance_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `username` varchar(50) NOT NULL COMMENT '唯一用户名',
  `password` varchar(100) NOT NULL COMMENT '加密密码',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态 0-未审核 1-待审核 2-通过 3-不通过',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='保险公司表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `insurance_company_audit_application`
--

DROP TABLE IF EXISTS `insurance_company_audit_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insurance_company_audit_application` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `apply_id` varchar(64) NOT NULL COMMENT '申请唯一标识（自定义唯一ID）',
  `insurance_company_id` bigint NOT NULL COMMENT '关联保险公司表ID',
  `insurance_company_full_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构全称',
  `social_credit_code` varchar(18) NOT NULL COMMENT '统一社会信用代码',
  `registered_address` varchar(500) NOT NULL COMMENT '注册地址',
  `insurance_company_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构类型：保险代理/保险经纪/渠道代理/其他',
  `business_license_no` varchar(100) NOT NULL COMMENT '经营许可证/备案号',
  `license_valid_start` date NOT NULL COMMENT '许可证有效期-开始',
  `license_valid_end` date NOT NULL COMMENT '许可证有效期-结束',
  `legal_person_name` varchar(50) NOT NULL COMMENT '法定代表人姓名',
  `legal_person_id_card` varchar(18) NOT NULL COMMENT '法定代表人身份证号',
  `legal_person_phone` varchar(20) NOT NULL COMMENT '法定代表人手机号',
  `contact_person_name` varchar(50) NOT NULL COMMENT '对接负责人姓名',
  `contact_person_position` varchar(50) NOT NULL COMMENT '对接负责人职务',
  `contact_person_phone` varchar(20) NOT NULL COMMENT '对接负责人手机号',
  `contact_person_office_phone` varchar(20) DEFAULT NULL COMMENT '对接负责人办公电话',
  `contact_person_email` varchar(100) NOT NULL COMMENT '对接负责人邮箱',
  `company_fixed_phone` varchar(20) NOT NULL COMMENT '公司固定电话',
  `business_scope` varchar(500) NOT NULL COMMENT '合作业务范围',
  `cooperation_area` varchar(50) NOT NULL COMMENT '合作区域：本市/本省/全国',
  `cooperation_mode` varchar(100) NOT NULL COMMENT '合作模式',
  `authenticity_commitment` tinyint NOT NULL COMMENT '资料真实性承诺：1-同意',
  `seal_file_path` varchar(500) NOT NULL COMMENT '申请单位盖章文件路径',
  `apply_date` date NOT NULL COMMENT '申请日期',
  `is_deleted` tinyint DEFAULT '0' COMMENT '软删除：0-未删 1-已删',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_id` (`apply_id`),
  KEY `idx_insurance_company_id` (`insurance_company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='保险公式合作审核申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nurse`
--

DROP TABLE IF EXISTS `nurse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurse` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `username` varchar(50) NOT NULL COMMENT '唯一用户名',
  `password` varchar(100) NOT NULL COMMENT '加密密码',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态 0-未审核 1-待审核 2-通过 3-不通过',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `online_status` tinyint NOT NULL DEFAULT '0' COMMENT '在线状态 0-离线 1-在线',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='护工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_admin`
--

DROP TABLE IF EXISTS `sys_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '管理员账号',
  `password` varchar(100) NOT NULL COMMENT '密码（建议加密存储）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'yx2_demo'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-19 14:42:56
