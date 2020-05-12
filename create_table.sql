drop database if exists onlinejudge;
create database onlinejudge;
use onlinejudge;

drop table if exists acm_contest_rank;
CREATE TABLE acm_contest_rank (
		id bigint AUTO_INCREMENT,
		contest_id bigint NOT NULL COMMENT '比赛id',
		user_id bigint NOT NULL COMMENT '用户id',
		locked bit NOT NULL COMMENT '用于区分封榜前后的榜',
		accepted_number int NOT NULL COMMENT '总AC数',
		submission_number int NOT NULL COMMENT '有效提交次数',
		total_time int NOT NULL COMMENT '总用时(秒)', 
		submission_info json NOT NULL COMMENT '{"问题id":{"is_ac":是否AC,"ac_time":第一次AC的相对时间(秒),"error_number": 罚时次数},...}例如{"9": {"is_ac": false, "ac_time": 0, "error_number": 2}}',
		PRIMARY KEY (id)
);

drop table if exists oi_contest_rank;
CREATE TABLE oi_contest_rank (
    id bigint AUTO_INCREMENT,
		contest_id bigint NOT NULL COMMENT '比赛id',
    user_id bigint NOT NULL COMMENT '用户id',
    submission_number int NOT NULL COMMENT '有效提交次数',
    total_score int NOT NULL COMMENT '总分',
    submission_info json NOT NULL COMMENT '{"问题id":总分,...}例如{"1":100,"3":20,"4":0}',
		PRIMARY KEY (id)
);

drop table if exists announcement;
CREATE TABLE announcement (
    id bigint AUTO_INCREMENT,
		sort_id int NOT NULL COMMENT '排序id',
    title text NOT NULL COMMENT '公告标题',
    content text NOT NULL COMMENT '公告内容',
		visible bit NOT NULL COMMENT '可见性',
    create_time timestamp NOT NULL COMMENT '创建时间',
    update_time timestamp NOT NULL COMMENT '最后一次修改时间',
		create_id bigint NOT NULL COMMENT '创建人',
		update_id bigint NOT NULL COMMENT '最后一次修改人',
		PRIMARY KEY (id)
);

drop table if exists contest;
CREATE TABLE contest (
    id bigint AUTO_INCREMENT,
		sort_id int DEFAULT 1 NOT NULL COMMENT '排序id',
    title text NOT NULL COMMENT '比赛标题',
    description text NOT NULL COMMENT '比赛介绍',
    real_time_rank bit NOT NULL COMMENT '赛中榜单查看权限(ACM/OI通用)',
    `password` text COMMENT '比赛密码(空则不需要)',
    rule_type text NOT NULL COMMENT '目前只有"ACM"/"OI"',
		penalty_time int NOT NULL COMMENT '每次错误提交的罚时',
    start_time timestamp NOT NULL COMMENT '开始时间',
    end_time timestamp NOT NULL COMMENT '结束时间',
		will_lock bit NOT NULL COMMENT '是否封榜(ACM)',
		lock_time timestamp COMMENT '封榜时间(ACM)',
		unlock_time timestamp COMMENT '解封时间(ACM/OI通用)',
		visible bit NOT NULL COMMENT '可见性',
    create_time timestamp NOT NULL COMMENT '创建时间',
    update_time timestamp NOT NULL COMMENT '最后一次修改时间',
    create_id bigint NOT NULL COMMENT '创建人',
		update_id bigint NOT NULL COMMENT '最后一次修改人',
		PRIMARY KEY (id)
);

drop table if exists contest_announcement;
CREATE TABLE contest_announcement (
    id bigint AUTO_INCREMENT,
		sort_id int NOT NULL COMMENT '排序id',
		contest_id bigint NOT NULL COMMENT '比赛id',
		problem_id bigint COMMENT '对应的赛题id,NULL则为General clarification',
    title text NOT NULL COMMENT '公告标题',
    content text NOT NULL COMMENT '公告内容',
		visible bit NOT NULL COMMENT '可见性',
-- 		visible_id bigint DEFAULT 0 NOT NULL COMMENT '可见的人id，默认0为全部',
    create_time timestamp NOT NULL COMMENT '创建时间',
    update_time timestamp COMMENT '最后一次更新时间',
		create_id bigint	NOT NULL COMMENT '创建人',
    update_id bigint COMMENT '最后一次更新人',
		PRIMARY KEY (id)
);

drop table if exists clarification;
CREATE TABLE clarification (
    id bigint AUTO_INCREMENT,
		contest_id bigint NOT NULL COMMENT '比赛id',
		problem_id bigint COMMENT '对应的赛题id,NULL则为General clarification',
		content text NOT NULL COMMENT '提问内容',
		create_id bigint NOT NULL COMMENT '提问人id',
    create_time timestamp NOT NULL COMMENT '提问时间',
		read_by_user bit NOT NULL COMMENT '用户阅读tag',
		read_by_admin bit NOT NULL COMMENT '管理阅读tag',
		PRIMARY KEY (id)
);

drop table if exists clarification_reply;
CREATE TABLE clarification_reply (
    id bigint AUTO_INCREMENT,
		clar_id bigint NOT NULL COMMENT '对应的clarification id',
		content text NOT NULL COMMENT '回复内容',
		create_id bigint NOT NULL COMMENT '回复人id',
    create_time timestamp NOT NULL COMMENT '回复时间',
		PRIMARY KEY (id)
);

drop table if exists problem;
CREATE TABLE problem (
    id bigint NOT NULL AUTO_INCREMENT,
		sort_id int COMMENT '排序id',
		`name` text COMMENT '题号',
    title text COMMENT '题目标题',
    description text COMMENT '问题描述',
    input_description text COMMENT '输入描述',
    output_description text COMMENT '输出描述',
    sample json  COMMENT '样例们[{"input":"...","output":"..."},...]',
    hint text COMMENT '提示信息',
		testcase_md5 text COMMENT 'testcase的md5,对应到本地的testcase/[md5]文件夹',
    allow_language json COMMENT '[{"language":"C++","factor":1.0},{"language":"Java","factor":2.0},{"language":"Python2","factor":3.0},{"language":"Python3","factor":3.0}]',
		tag json COMMENT '["tag1","tag2",...]',
    time_limit int  COMMENT '时间限制(ms)',
    memory_limit int  COMMENT '空间限制(MB)',
    visible bit DEFAULT 1 COMMENT '可见性',
    submission_number int DEFAULT 0 COMMENT '题库提交次数',
    accepted_number int DEFAULT 0 COMMENT '题库AC次数(同一人的ac不重复计算)',
		
		create_time timestamp COMMENT '题目创建时间',
    update_time timestamp COMMENT '最后一次更新时间',
		create_id bigint COMMENT '题目创建人',
    update_id bigint COMMENT '最后一次更新人',
		
		spj bit COMMENT '是否spj',
    spj_language text COMMENT 'spj语言',
    spj_code text COMMENT 'spj源代码',
		spj_md5 text COMMENT 'spj源代码的md5',
		
		in_contest bit DEFAULT 0  COMMENT '是否是赛题',
		contest_id bigint COMMENT '比赛id',
		problem_id bigint COMMENT '题库题目id',
		submission_number_locked int DEFAULT 0  COMMENT '封榜前赛中提交次数',
		accepted_number_locked int DEFAULT 0  COMMENT '封榜前赛中AC次数(同一人的ac不重复计算)',
		PRIMARY KEY (id)
);


-- drop table if exists tag;
-- CREATE TABLE tag (
-- 		problem_id bigint NOT NULL COMMENT '问题id',
--     `name` text NOT NULL COMMENT 'tag内容',
-- 		PRIMARY KEY (problem_id,`name`(50))
-- );

drop table if exists submission;
CREATE TABLE submission (
    id bigint AUTO_INCREMENT,
		in_contest bit DEFAULT 0 NOT NULL COMMENT '是否是比赛提交',
    contest_id bigint COMMENT '对应的比赛id,0为题库',
    problem_id bigint NOT NULL COMMENT '对应的题目id,比赛则为赛题id,题库则为题目id',
    user_id bigint NOT NULL COMMENT '提交人',
		`language` text NOT NULL COMMENT '语言',
    `code` text NOT NULL COMMENT '源代码',
		create_time timestamp NOT NULL COMMENT '提交时间',
    `status` int NOT NULL COMMENT '评测状态/结果',
		execute_time int COMMENT '最大运行时间',
		execute_memory int COMMENT '峰值内存',
		code_length int NOT NULL COMMENT '代码长度',
    detail json COMMENT '每个点的状态',
		judge_time timestamp COMMENT '评测完成时间',
		PRIMARY KEY (id)
);

drop table if exists `user`;
CREATE TABLE `user` (
    id bigint AUTO_INCREMENT, 
		username text NOT NULL COMMENT '登录名',
    `password` text NOT NULL COMMENT '加密后的密码',
		create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '账户创建时间',
    last_login_time timestamp COMMENT '最后一次登录时间',
    email text COMMENT '邮箱地址(用于找回密码/登录)',
    disabled bit DEFAULT 0 NOT NULL COMMENT '封禁',
    role json NOT NULL COMMENT '账户角色,["admin","user"]',
		nickname text NOT NULL COMMENT '昵称',
		realname text NOT NULL COMMENT '真实信息',
		avatar mediumtext COMMENT '头像',
		accepted_id json NOT NULL COMMENT '已AC的题库题目id',
		accepted_number int DEFAULT 0 NOT NULL COMMENT '已AC的题库题目数',
		submission_number int DEFAULT 0 NOT NULL COMMENT '有效提交次数',
		PRIMARY KEY (id)
);

-- 不需要手动创建简单视图
-- create view user_info as select id,username,nickname,realname from user;

-- auth_group,auth_group_permissions,auth_permission三表，废弃的权限控制模块

-- CREATE TABLE public.auth_group (
--     id integer NOT NULL,
--     name character varying(80) NOT NULL
-- );
-- CREATE TABLE public.auth_group_permissions (
--     id integer NOT NULL,
--     group_id integer NOT NULL,
--     permission_id integer NOT NULL
-- );
-- CREATE TABLE public.auth_permission (
--     id integer NOT NULL,
--     name character varying(255) NOT NULL,
--     content_type_id integer NOT NULL,
--     codename character varying(100) NOT NULL
-- );