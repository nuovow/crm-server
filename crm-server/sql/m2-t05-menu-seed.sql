-- M2-T05: 用户/角色管理菜单种子 + 数据看板入口
INSERT INTO sys_menu (id, parent_id, name, type, permission, path, sort) VALUES
(300, 0,   '数据看板', 1, NULL, '/dashboard', 0),
(202, 200, '角色管理', 1, NULL, '/role', 2),
(2011, 201, '新增用户', 2, 'user:add',        NULL, 1),
(2012, 201, '编辑用户', 2, 'user:edit',       NULL, 2),
(2013, 201, '删除用户', 2, 'user:delete',     NULL, 3),
(2014, 201, '分配角色', 2, 'user:assignRole', NULL, 4),
(2021, 202, '新增角色', 2, 'role:add',        NULL, 1),
(2022, 202, '编辑角色', 2, 'role:edit',       NULL, 2),
(2023, 202, '删除角色', 2, 'role:delete',     NULL, 3),
(2024, 202, '分配权限', 2, 'role:assignMenu', NULL, 4);

-- ADMIN(1): 追加全部新菜单；SALES(2)/MANAGER(3): 追加看板入口
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES
(14, 1, 300), (15, 1, 202), (16, 1, 2011), (17, 1, 2012), (18, 1, 2013), (19, 1, 2014),
(20, 1, 2021), (21, 1, 2022), (22, 1, 2023), (23, 1, 2024),
(24, 2, 300),
(25, 3, 100), (26, 3, 101), (27, 3, 300);
