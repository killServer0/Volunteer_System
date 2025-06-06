
-- 更新现有活动的状态为"未开始"
UPDATE `feihuodong` SET `huodong_status` = 2 WHERE `huodong_status` IS NULL;
UPDATE `zhihuodong` SET `huodong_status` = 2 WHERE `huodong_status` IS NULL;

-- 添加活动状态到字典表
INSERT INTO `dictionary` (`dic_code`, `dic_name`, `code_index`, `index_name`, `super_id`, `beizhu`, `create_time`) VALUES
('huodong_status', '活动状态', 1, '待审核', NULL, NULL, NOW()),
('huodong_status', '活动状态', 2, '未开始', NULL, NULL, NOW()),
('huodong_status', '活动状态', 3, '进行中', NULL, NULL, NOW()),
('huodong_status', '活动状态', 4, '已结束', NULL, NULL, NOW()); 