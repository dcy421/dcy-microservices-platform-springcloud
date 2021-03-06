/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50712
 Source Host           : localhost:3306
 Source Schema         : dcy-microservices-platform

 Target Server Type    : MySQL
 Target Server Version : 50712
 File Encoding         : 65001

 Date: 19/02/2020 14:30:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件md5',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `is_img` tinyint(1) NOT NULL COMMENT '是否是图片类型',
  `content_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',
  `size` int(11) NOT NULL COMMENT '文件大小',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物理路径',
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'url地址',
  `source` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源',
  `create_date` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_info
-- ----------------------------
INSERT INTO `file_info` VALUES ('2c95b54f4d8356cf8ab40802f496df83', '头像.png', 1, 'image/png', 1290, 'http://pkqtmn0p1.bkt.clouddn.com/头像.png', 'http://pkqtmn0p1.bkt.clouddn.com/头像.png', 'QINIU', '20190108170536');
INSERT INTO `file_info` VALUES ('9650357b6b77eba5db65e7593c75bfc5', '壁纸六.png', 1, 'image/png', 54508, 'http://py9cmj9ml.bkt.clouddn.com//壁纸六.png', 'http://py9cmj9ml.bkt.clouddn.com//壁纸六.png', 'qiniu', '20190923081011960');
INSERT INTO `file_info` VALUES ('d9c81d9c4a45fc58520f14602b5c1687', '旭旭宝宝头像.jpg', 1, 'image/jpeg', 33072, 'group1/M00/00/00/wKi9t13KXoKABjKRAACBMMJ7Ivo638.jpg', 'http://192.168.189.183:8888/group1/M00/00/00/wKi9t13KXoKABjKRAACBMMJ7Ivo638.jpg', 'fastdfs', '20191112152554184');

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `resource_ids` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `client_secret` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `scope` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `authorized_grant_types` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `authorities` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `autoapprove` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'OAuth客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('dcy_admin_client', NULL, '{bcrypt}$2a$10$Z/wu.FZ22lY8TCDkPkdgmekhZn2zKJMxic2P50lf4Aajomxq1NNIa', 'dcy_admin', 'password,authorization_code,refresh_token,implicit', NULL, NULL, NULL, NULL, NULL, 'true');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数id',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数键名',
  `config_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数键值',
  `config_type` tinyint(20) NULL DEFAULT NULL COMMENT '系统内置',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `dict_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典主键',
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `parent_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级ids',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `dict_lable` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典键值',
  `location` decimal(20, 2) NULL DEFAULT NULL COMMENT '排序',
  `has_children` tinyint(11) NULL DEFAULT NULL COMMENT '子节点',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子类型',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '0', '0', 'sex_group', '性别', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('10', '0', '0', 'menu_type_group', '权限类型', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('11', '10', '0,10', 'menu_type_group', '模块权限', '0', 0.00, NULL, 'menu_type', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('12', '10', '0,10', 'menu_type_group', '菜单权限', '1', 1.00, NULL, 'menu_type', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('13', '0', '0', 'module_type_group', '模块类型', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('14', '13', '0,13', 'module_type_group', '模块', '0', 0.00, NULL, 'module_type', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('15', '13', '0,13', 'module_type_group', '操作', '1', 1.00, NULL, 'module_type', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('16', '0', '0', 'http_method_group', '请求方式', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('17', '16', '0,16', 'http_method_group', 'GET', 'GET', 0.00, NULL, 'http_method', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('18', '16', '0,16', 'http_method_group', 'POST', 'POST', 1.00, NULL, 'http_method', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('2', '1', '0,1', 'sex_group', '男', '0', 0.00, NULL, 'sex', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('3', '1', '0,1', 'sex_group', '女', '1', 1.00, NULL, 'sex', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('4', '0', '0', 'status_group', '状态', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('5', '4', '0,4', 'status_group', '正常', '0', 0.00, NULL, 'status', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('6', '4', '0,4', 'status_group', '禁用', '1', 1.00, NULL, 'status', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('7', '0', '0', 'power_type_group', '菜单类型', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('8', '7', '0,7', 'power_type_group', '模块', '0', 0.00, NULL, 'power_type', NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_dict` VALUES ('9', '7', '0,7', 'power_type_group', '菜单', '1', 1.00, NULL, 'power_type', NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单id',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级id',
  `parent_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级ids',
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限code',
  `type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `href` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `target` tinyint(11) NULL DEFAULT NULL COMMENT '打开方式',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `disabled_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '禁用',
  `location` decimal(20, 2) NULL DEFAULT NULL COMMENT '排序',
  `has_children` tinyint(11) NULL DEFAULT NULL COMMENT '是否有子节点',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '全部', '0', '0', NULL, '0', NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176303606933008386', '用户管理', '3', '0,1,2,3', 'user:manage', '1', '/user', NULL, 'icon', '0', 10.00, NULL, '1170896100656156674', '20190924091329214', '1170896100656156674', '20190924105725605', 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176304192273297409', '角色管理', '3', '0,1,2,3', 'role:manage', '1', '/role', NULL, 'icon', '0', 20.00, NULL, '1170896100656156674', '20190924091548771', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176307088410849282', '用户组管理', '3', '0,1,2,3', 'group:manage', '1', '/group', NULL, 'icon', '0', 30.00, NULL, '1170896100656156674', '20190924092719264', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176307183164370946', '权限管理', '3', '0,1,2,3', 'power:manage', '1', '/power', NULL, 'icon', '0', 40.00, NULL, '1170896100656156674', '20190924092741856', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176307312856444930', '菜单管理', '3', '0,1,2,3', 'menu:manage', '1', '/menu', NULL, NULL, '0', 50.00, NULL, '1170896100656156674', '20190924092812775', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176307416136986626', '模块管理', '3', '0,1,2,3', 'module:manage', '1', '/module', NULL, NULL, '0', 60.00, NULL, '1170896100656156674', '20190924092837400', '1170896100656156674', '20190924092839713', 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176327576596000770', 'swagger文档', '3', '0,1,2,3', 'swagger:manage', '1', '/swagger', NULL, 'icon', '0', 70.00, NULL, '1170896100656156674', '20190924104844028', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176327996827512834', 'admin监控', '3', '0,1,2,3', 'admin:manage', '1', '/admin', NULL, 'icon', '0', 80.00, NULL, '1170896100656156674', '20190924105024218', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176328176683462657', 'druid监控', '3', '0,1,2,3', 'druid:manage', '1', '/druid', NULL, 'icon', '0', 90.00, NULL, '1170896100656156674', '20190924105107099', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('1176328289493463041', 'nacos注册中心', '3', '0,1,2,3', 'nacos:manage', '1', '/nacos', NULL, NULL, '0', 100.00, NULL, '1170896100656156674', '20190924105133995', NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('2', '系统模块', '1', '0,1', NULL, '0', NULL, NULL, NULL, '0', 100.00, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_menu` VALUES ('3', '系统管理', '2', '0,1,2', NULL, '0', NULL, NULL, NULL, '0', 10.00, NULL, NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_module_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_module_resources`;
CREATE TABLE `sys_module_resources`  (
  `module_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `parent_ids` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级ids',
  `module_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `module_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块code',
  `module_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块path',
  `http_method` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `module_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `module_sort` decimal(11, 2) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`module_id`) USING BTREE,
  INDEX `FK_Reference_5`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_module_resources
-- ----------------------------
INSERT INTO `sys_module_resources` VALUES ('1', '0', '0', '全部', 'ALL', NULL, NULL, '0', '0', NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173416959543459841', '1', '0,1', '系统模块', NULL, NULL, NULL, '0', '0', 100.00, '1170896100656156674', '20190916100258845', '1170896100656156674', '20190916100519194', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173417638056017922', '1173416959543459841', '0,1,1173416959543459841', '系统管理', NULL, NULL, NULL, '0', '0', 100.00, '1170896100656156674', '20190916100540615', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173417760584220674', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '用户管理', NULL, NULL, NULL, '0', '0', 10.00, '1170896100656156674', '20190916100609829', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173418168740331521', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '查询所有用户', 'user:list', '/admin-center/user/all', 'GET', '0', '1', 1.00, '1170896100656156674', '20190916100747142', '1170896100656156674', '20191223101703904', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173494683788189698', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '添加用户', 'user:save', '/admin-center/user/save', 'POST', '0', '1', 2.00, '1170896100656156674', '20190916151149750', '1170896100656156674', '20191223101218219', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173519815223037953', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '修改用户', 'user:update', '/admin-center/user/update', 'POST', '0', '1', 5.00, '1170896100656156674', '20190916165141551', '1170896100656156674', '20191223101413561', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173519895434907649', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '删除用户', 'user:delete', '/admin-center/user/delete', 'POST', '0', '1', 7.00, '1170896100656156674', '20190916165200675', '1170896100656156674', '20191223101422453', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173519984786165761', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '批量删除用户', 'user:batch:delete', '/admin-center/user/deleteBatch', 'POST', '0', '1', 12.00, '1170896100656156674', '20190916165221978', '1170896100656156674', '20191223101627487', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520061013446658', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '重置密码', 'user:reset:pass', '/admin-center/user/resetPassword', 'POST', '0', '1', 13.00, '1170896100656156674', '20190916165240153', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520174125436930', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '根据用户id查询已授权的角色列表', 'user:auth:role', '/admin-center/user/getAuthRoleListByUserId', 'GET', '0', '1', 15.00, '1170896100656156674', '20190916165307120', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520251158024193', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '保存授权角色', 'user:auth:role', '/admin-center/user/saveAuthRole', 'POST', '0', '1', 15.00, '1170896100656156674', '20190916165325486', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520335761330177', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '根据用户id查询已授权的用户组列表', 'user:auth:group', '/admin-center/user/getAuthGroupListByUserId', 'GET', '0', '1', 18.00, '1170896100656156674', '20190916165345657', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520501490864130', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '保存授权用户组', 'user:auth:group', '/admin-center/user/saveAuthGroup', 'POST', '0', '1', 20.00, '1170896100656156674', '20190916165425170', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173520583535644673', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '根据用户名获取用户信息', NULL, '/admin-center/user/getUserInfoByUsername', 'GET', '0', '1', 23.00, '1170896100656156674', '20190916165444731', '1170896100656156674', '20190916165844972', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173521725967654914', '1173417760584220674', '0,1,1173416959543459841,1173417638056017922,1173417760584220674', '获取用户分页信息', 'user:list', '/admin-center/user/page', 'GET', '0', '1', 1.00, '1170896100656156674', '20190916165917108', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173786240433037313', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '角色管理', NULL, NULL, NULL, '0', '0', 20.00, '1170896100656156674', '20190917103022271', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173786513016659970', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '查询所有角色', 'role:list', '/admin-center/role/all', 'GET', '0', '1', 5.00, '1170896100656156674', '20190917103127259', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173786896229244930', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '获取角色分页信息', 'role:list', '/admin-center/role/page', 'GET', '0', '1', 3.00, '1170896100656156674', '20190917103258624', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787141281456130', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '添加角色', 'role:save', '/admin-center/role/save', 'POST', '0', '1', 7.00, '1170896100656156674', '20190917103357050', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787273578192898', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '修改角色', 'role:update', '/admin-center/role/update', 'POST', '0', '1', 10.00, '1170896100656156674', '20190917103428591', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787371326447617', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '删除角色', 'role:delete', '/admin-center/role/delete', 'POST', '0', '1', 15.00, '1170896100656156674', '20190917103451898', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787467954823170', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '批量删除角色', 'role:batch:delete', '/admin-center/role/deleteBatch', 'POST', '0', '1', 17.00, '1170896100656156674', '20190917103514936', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787618282872833', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '根据角色id查询已授权的权限列表', 'role:auth:power', '/admin-center/role/getAuthPowerListByRoleId', 'GET', '0', '1', 20.00, '1170896100656156674', '20190917103550776', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787686142517250', '1173786240433037313', '0,1,1173416959543459841,1173417638056017922,1173786240433037313', '保存授权权限', 'role:auth:power', '/admin-center/role/saveAuthPower', 'POST', '0', '1', 25.00, '1170896100656156674', '20190917103606955', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173787943672782849', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '用户组管理', NULL, NULL, NULL, '0', '0', 30.00, '1170896100656156674', '20190917103708354', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788126460551170', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '获取所有用户组', 'group:list', '/admin-center/group/all', 'GET', '0', '1', 4.00, '1170896100656156674', '20190917103751934', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788256727244801', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '获取用户组分页信息', 'group:list', '/admin-center/group/page', 'GET', '0', '1', 6.00, '1170896100656156674', '20190917103822993', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788527679283201', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '添加用户组', 'group:add', '/admin-center/group/save', 'POST', '0', '1', 10.00, '1170896100656156674', '20190917103927593', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788605882081282', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '修改用户组', 'group:update', '/admin-center/group/update', 'POST', '0', '1', 14.00, '1170896100656156674', '20190917103946238', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788683799666689', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '删除用户组', 'group:delete', '/admin-center/group/delete', 'POST', '0', '1', 16.00, '1170896100656156674', '20190917104004815', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788787419947009', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '批量删除用户组', 'group:batch:delete', '/admin-center/group/deleteBatch', 'POST', '0', '1', 18.00, '1170896100656156674', '20190917104029520', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788881565294593', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '根据用户组id查询已授权的角色列表', 'group:auth:role', '/admin-center/group/getAuthRoleListByUserGroupId', 'GET', '0', '1', 20.00, '1170896100656156674', '20190917104051966', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173788959784869890', '1173787943672782849', '0,1,1173416959543459841,1173417638056017922,1173787943672782849', '保存授权角色', 'group:auth:role', '/admin-center/group/saveAuthRole', 'POST', '0', '1', 24.00, '1170896100656156674', '20190917104110615', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173789224663556098', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '权限管理', NULL, NULL, NULL, '0', '0', 40.00, '1170896100656156674', '20190917104213767', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173789352040374273', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '获取所有权限', 'power:list', '/admin-center/power/all', 'GET', '0', '1', 3.00, '1170896100656156674', '20190917104244136', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173789493967233026', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '获取权限分页信息', 'power:list', '/admin-center/power/page', 'GET', '0', '1', 5.00, '1170896100656156674', '20190917104317973', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792306080292866', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '添加权限', 'power:add', '/admin-center/power/save', 'POST', '0', '1', 7.00, '1170896100656156674', '20190917105428433', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792384446668801', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '修改权限', 'power:update', '/admin-center/power/update', 'POST', '0', '1', 10.00, '1170896100656156674', '20190917105447118', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792467393224705', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '删除权限', 'power:delete', '/admin-center/power/delete', 'POST', '0', '1', 12.00, '1170896100656156674', '20190917105506894', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792556153085953', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '批量删除权限', 'power:batch:delete', '/admin-center/power/deleteBatch', 'POST', '0', '1', 14.00, '1170896100656156674', '20190917105528055', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792636218155010', '1173789224663556098', '0,1,1173416959543459841,1173417638056017922,1173789224663556098', '保存授权模块', 'power:auth:module', '/admin-center/power/saveAuthModule', 'POST', '0', '1', 16.00, '1170896100656156674', '20190917105547144', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792738055856130', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '模块管理', NULL, NULL, NULL, '0', '0', 60.00, '1170896100656156674', '20190917105611425', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173792954100260865', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '获取所有模块', 'module:list', '/admin-center/module/all', 'GET', '0', '1', 2.00, '1170896100656156674', '20190917105702934', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793058425184258', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '获取模块分页信息', 'module:list', '/admin-center/module/page', 'GET', '0', '1', 5.00, '1170896100656156674', '20190917105727807', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793141136859137', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '添加模块', 'module:add', '/admin-center/module/save', 'POST', '0', '1', 7.00, '1170896100656156674', '20190917105747527', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793218580488194', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '修改模块', 'module:update', '/admin-center/module/update', 'POST', '0', '1', 9.00, '1170896100656156674', '20190917105805991', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793287136387073', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '删除模块', 'module:delete', '/admin-center/module/delete', 'POST', '0', '1', 11.00, '1170896100656156674', '20190917105822335', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793356451454977', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '批量删除模块', 'module:batch:delete', '/admin-center/module/deleteBatch', 'POST', '0', '1', 13.00, '1170896100656156674', '20190917105838862', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793449908936705', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '获取模块tree-table数据', 'module:list', '/admin-center/module/getModuleTreeTableList', 'GET', '0', '1', 15.00, '1170896100656156674', '20190917105901144', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1173793663180906497', '1173792738055856130', '0,1,1173416959543459841,1173417638056017922,1173792738055856130', '根据权限id，查询模块tree列表数据', 'module:list', '/admin-center/module/getModuleTreeListByPowerId', 'GET', '0', '1', 18.00, '1170896100656156674', '20190917105951992', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208937097818279937', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '菜单管理', NULL, NULL, NULL, '0', '0', 50.00, '1170896100656156674', '20191223102719743', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208937442342604802', '1208937097818279937', '0,1,1173416959543459841,1173417638056017922,1208937097818279937', '获取tree-table菜单列表数据', 'menu:list', '/admin-center/menu/getMenuTreeTableList', 'GET', '0', '1', 5.00, '1170896100656156674', '20191223102841883', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208937654247231489', '1208937097818279937', '0,1,1173416959543459841,1173417638056017922,1208937097818279937', '添加菜单', 'menu:add', '/admin-center/menu/add', 'POST', '0', '1', 7.00, '1170896100656156674', '20191223102932406', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208937794592837634', '1208937097818279937', '0,1,1173416959543459841,1173417638056017922,1208937097818279937', '修改菜单', 'menu:update', '/admin-center/menu/update', 'POST', '0', '1', 9.00, '1170896100656156674', '20191223103005866', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208937968912306177', '1208937097818279937', '0,1,1173416959543459841,1173417638056017922,1208937097818279937', '删除菜单', 'menu:delete', '/admin-center/menu/delete', 'POST', '0', '1', 11.00, '1170896100656156674', '20191223103047427', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208938290946772993', '1173417638056017922', '0,1,1173416959543459841,1173417638056017922', '字典管理', NULL, NULL, NULL, '0', '0', 70.00, '1170896100656156674', '20191223103204207', NULL, NULL, 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208938414578077698', '1208938290946772993', '0,1,1173416959543459841,1173417638056017922,1208938290946772993', '获取tree-table字典列表数据', 'dict:list', '/admin-center/dict/getDictTreeTableList', 'GET', '0', '1', 5.00, '1170896100656156674', '20191223103233682', '1170896100656156674', '20191223103250460', 0, NULL);
INSERT INTO `sys_module_resources` VALUES ('1208938659106000898', '1208938290946772993', '0,1,1173416959543459841,1173417638056017922,1208938290946772993', '删除字典', 'dict:delete', '/admin-center/dict/delete', 'POST', '0', '1', 10.00, '1170896100656156674', '20191223103331982', NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation`;
CREATE TABLE `sys_operation`  (
  `oper_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作id',
  `oper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作名字',
  `oper_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作代码',
  `parent_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级id',
  `parent_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级ids',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '功能操作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `log_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志id',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `operator_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类别',
  `oper_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `status` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作状态',
  `error_msg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `oper_time` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_power
-- ----------------------------
DROP TABLE IF EXISTS `sys_power`;
CREATE TABLE `sys_power`  (
  `pow_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限id',
  `pow_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `pow_type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限类型',
  `pow_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '权限状态',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pow_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_power
-- ----------------------------
INSERT INTO `sys_power` VALUES ('1173423204782419969', '管理员模块权限', '0', '0', '1170896100656156674', '20190916102747827', '1170896100656156674', '20190916103805021', 0, NULL);
INSERT INTO `sys_power` VALUES ('1176317437520384002', '管理员菜单权限', '1', '0', '1170896100656156674', '20190924100826684', '1170896100656156674', '20190924101615083', 0, NULL);

-- ----------------------------
-- Table structure for sys_power_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_power_menu`;
CREATE TABLE `sys_power_menu`  (
  `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单id',
  `pow_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_power_menu
-- ----------------------------
INSERT INTO `sys_power_menu` VALUES ('1176303606933008386', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176304192273297409', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176307088410849282', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176307183164370946', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176307312856444930', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176307416136986626', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176327576596000770', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176327996827512834', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176328176683462657', '1176317437520384002');
INSERT INTO `sys_power_menu` VALUES ('1176328289493463041', '1176317437520384002');

-- ----------------------------
-- Table structure for sys_power_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_power_module`;
CREATE TABLE `sys_power_module`  (
  `pow_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限id',
  `module_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_power_module
-- ----------------------------
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173418168740331521');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173521725967654914');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173494683788189698');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173519815223037953');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173519895434907649');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173519984786165761');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520061013446658');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520174125436930');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520251158024193');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520335761330177');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520501490864130');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173520583535644673');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173786896229244930');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173786513016659970');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787141281456130');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787273578192898');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787371326447617');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787467954823170');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787618282872833');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173787686142517250');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788126460551170');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788256727244801');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788527679283201');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788605882081282');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788683799666689');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788787419947009');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788881565294593');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173788959784869890');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173789352040374273');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173789493967233026');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792306080292866');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792384446668801');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792467393224705');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792556153085953');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792636218155010');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1208937442342604802');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1208937654247231489');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1208937794592837634');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1208937968912306177');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173792954100260865');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793058425184258');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793141136859137');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793218580488194');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793287136387073');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793356451454977');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793449908936705');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1173793663180906497');
INSERT INTO `sys_power_module` VALUES ('1173423204782419969', '1208938414578077698');

-- ----------------------------
-- Table structure for sys_power_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_power_operation`;
CREATE TABLE `sys_power_operation`  (
  `pow_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `oper_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限功能关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `role_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色状态（0、正常；1、禁用）',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1171709223680184321', '管理员', 'ROLE_ADMIN', '0', NULL, '20190911165702888', '1170896100656156674', '20191203133702476', 0, '备注');
INSERT INTO `sys_role` VALUES ('1171953892250918913', '开发组长', 'ROLE_DEVELOP', '0', NULL, '20190912090916421', NULL, NULL, 0, NULL);
INSERT INTO `sys_role` VALUES ('1171953965877731330', '测试组长', 'ROLE_TEST', '0', NULL, '20190912090933975', NULL, NULL, 0, NULL);
INSERT INTO `sys_role` VALUES ('1171954063797952514', '项目经理', 'ROLE_MANAGE', '1', NULL, '20190912090957320', NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_role_power
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_power`;
CREATE TABLE `sys_role_power`  (
  `role_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `pow_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_power
-- ----------------------------
INSERT INTO `sys_role_power` VALUES ('1171709223680184321', '1176317437520384002');
INSERT INTO `sys_role_power` VALUES ('1171709223680184321', '1173423204782419969');

-- ----------------------------
-- Table structure for sys_user_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_group`;
CREATE TABLE `sys_user_group`  (
  `user_group_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户组id',
  `user_group_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户组名',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_group
-- ----------------------------
INSERT INTO `sys_user_group` VALUES ('1171713103138754562', '开发用户组', NULL, '20190911171227822', NULL, '20190912094052154', 0, '备注');
INSERT INTO `sys_user_group` VALUES ('1171962001388322817', '测试用户组', NULL, '20190912094129790', NULL, NULL, 0, NULL);
INSERT INTO `sys_user_group` VALUES ('1171962053691293698', '管理员用户组', NULL, '20190912094142259', NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_user_group_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_group_role`;
CREATE TABLE `sys_user_group_role`  (
  `user_group_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户组id',
  `role_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_group_role
-- ----------------------------
INSERT INTO `sys_user_group_role` VALUES ('1171962053691293698', '1171953892250918913');
INSERT INTO `sys_user_group_role` VALUES ('1171962053691293698', '1171953965877731330');
INSERT INTO `sys_user_group_role` VALUES ('1171962053691293698', '1171709223680184321');
INSERT INTO `sys_user_group_role` VALUES ('1171713103138754562', '1171953892250918913');

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
  `user_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '用户类型（0、管理员；1、普通用户）',
  `email` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone_number` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sex` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别（0、男；1、女）',
  `avatar_path` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `user_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0、正常；1、禁用）',
  `create_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(11) NULL DEFAULT 0 COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------
INSERT INTO `sys_user_info` VALUES ('1170896100656156674', 'admin', '{bcrypt}$2a$10$Z/wu.FZ22lY8TCDkPkdgmekhZn2zKJMxic2P50lf4Aajomxq1NNIa', '管理员', '0', '13223423@qq.com', '15988888888', '0', NULL, '0', NULL, '20190909110559252', '1170896100656156674', '20190911173004063', 0, '管理员');
INSERT INTO `sys_user_info` VALUES ('1171948965562806274', '123456', '{bcrypt}$2a$10$5xasCEn4qiTY6814z3l87.tD9hw4BBj9rCx/4G8ViGoqnovPybMgK', 'dd', '1', 'dsfa', '112312', '0', NULL, '0', '1170896100656156674', '20190912084941807', '1170896100656156674', '20190916161125402', 0, 'dd');
INSERT INTO `sys_user_info` VALUES ('1173480636380426241', '123456', '{bcrypt}$2a$10$h9ft5OlLp/4tPpkwbmCCpOwT0.sSbgjULmsK8WkR7DgsxdG6lXlTS', '121', '1', '11114', '121212121', '0', NULL, '0', '1170896100656156674', '20190916141600586', '1170896100656156674', '20190916141613195', 1, '12121');
INSERT INTO `sys_user_info` VALUES ('string', 'string', '{bcrypt}$2a$10$BNDwPsPKWhQoRBQAmhWbYu2E3l3STEBMmjdb1fZY0UsDFLYSrdx6K', 'string', 'string', 'string', 'string', 'string', 'string', 'string', 'string', 'string', 'string', 'string', 1, 'string');

-- ----------------------------
-- Table structure for sys_user_info_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info_group`;
CREATE TABLE `sys_user_info_group`  (
  `user_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `user_group_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户组id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和用户组关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info_group
-- ----------------------------
INSERT INTO `sys_user_info_group` VALUES ('1170896100656156674', '1171962053691293698');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1171948965562806274', '1171709223680184321');
INSERT INTO `sys_user_role` VALUES ('1170896100656156674', '1171709223680184321');

-- ----------------------------
-- Function structure for FORMAT_DATE
-- ----------------------------
DROP FUNCTION IF EXISTS `FORMAT_DATE`;
delimiter ;;
CREATE FUNCTION `FORMAT_DATE`(date_str VARCHAR(50),type int)
 RETURNS varchar(50) CHARSET utf8
BEGIN
    -- DECLARE str1 VARCHAR(255);	-- 声明一个变量
    -- 返回值
		DECLARE str VARCHAR(50);
		CASE type
		WHEN 1 THEN
			set @str = CONCAT(MID(date_str,1,4),'-',+MID(date_str,5,2),'-',MID(date_str,7,2));
		WHEN 2 THEN
			set @str =  CONCAT(MID(date_str,1,4),'-',+MID(date_str,5,2),'-',MID(date_str,7,2),' ',MID(date_str,9,2),':',+MID(date_str,11,2),':',MID(date_str,13,2));
		ELSE
			set @str = CONCAT(MID(date_str,1,4),'-',+MID(date_str,5,2),'-',MID(date_str,7,2));
		END CASE;
	RETURN @str;
-- 结束定义
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
