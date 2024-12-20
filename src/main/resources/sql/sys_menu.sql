INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (4, 0, '系统管理', NULL, NULL, NULL, 0, 'system', 1, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (5, 1, '用户管理', '/user', 'user:list,user:create', 'UserComponent', 1, 'user', 2, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (6, 1, '角色管理', '/role', 'role:list,role:create', 'RoleComponent', 1, 'role', 3, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (7, 1, '菜单管理', '/menu', 'menu:list,menu:create', 'MenuComponent', 1, 'menu', 4, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (8, 1, '部门管理', '/dept', 'dept:list,dept:create', 'DeptComponent', 1, 'dept', 5, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (9, 1, '岗位管理', '/post', 'post:list,post:create', 'PostComponent', 1, 'post', 6, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (10, 1, '字典管理', '/dict', 'dict:list,dict:create', 'DictComponent', 1, 'dict', 7, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (11, 1, '参数设置', '/setting', 'setting:list,setting:create', 'SettingComponent', 1, 'setting', 8, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (12, 0, '通知公告', '/announcement', NULL, NULL, 1, 'announcement', 9, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (13, 0, '日志管理', NULL, NULL, NULL, 0, 'log', 10, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (14, 10, '操作日志', '/log/operate', 'log:operate', 'OperateLogComponent', 1, 'log-operate', 11, '2024-12-19 23:25:07', NULL, 1);
INSERT INTO `sys_menu`(`id`, `parent_id`, `name`, `path`, `perms`, `component`, `type`, `icon`, `orderNum`, `created`, `updated`, `status`) VALUES (15, 10, '登录日志', '/log/login', 'log:login', 'LoginLogComponent', 1, 'log-login', 12, '2024-12-19 23:25:07', NULL, 1);