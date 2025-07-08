-- 创建库
create database if not exists biaohui character set utf8mb4 collate utf8mb4_unicode_ci;

-- 切换库
use biaohui;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    user_name     varchar(256)                           null comment '用户昵称',
    user_account  varchar(256)                           not null comment '账号',
    user_avatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    user_password varchar(512)                           not null comment '密码',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_user_account
        unique (user_account)
) comment '用户';

create index idx_user_name on user (user_name);


-- auto-generated definition
create table oper_log
(
    id           bigint auto_increment
        primary key,
    title        varchar(50)  default ''                null comment '模块标题',
    business_type char         default '0'               null comment '业务类型（0其他 1新增 2修改 3删除）',
    oper_method   varchar(100) default ''                null comment '方法名称',
    oper_name     varchar(50)  default ''                null comment '操作人员',
    oper_url      varchar(255) default ''                null comment '请求URL',
    oper_ip       varchar(128) default ''                null comment '请求ip',
    oper_param    text                                   null comment '请求参数',
    json_result   text                                   null comment '返回参数',
    status       char         default '0'               null comment '操作状态（0正常 1异常）',
    error_msg     text                                   null comment '错误信息',
    oper_time     datetime    default CURRENT_TIMESTAMP not null comment '操作时间',
    cost_time     bigint       default 0                 null comment '消耗时间'
)
    comment '日志操作表';

create index idx_business_type
    on oper_log (business_type);

create index idx_oper_time
    on oper_log (oper_time);

-- auto-generated definition
create table login_info
(
    id            bigint auto_increment
        primary key,
    user_name      varchar(50)  default ''                null comment '用户名',
    ip_addr        varchar(128) default ''                null comment 'ip地址',
    login_location varchar(255) default ''                null comment '登陆地址',
    browser       varchar(50)  default ''                null comment '浏览器信息',
    os            varchar(50)  default ''                null comment '系统信息',
    status        char         default '0'               null comment '登陆状态',
    msg           varchar(255) default '提示消息'        null,
    login_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '访问时间'
) comment '登陆日志表';

create index idx_loginTime
    on login_info (login_time);


-- auto-generated definition
create table menu
(
    id          bigint auto_increment
        primary key,
    parent_id   bigint                              not null comment '父菜单id',
    name        varchar(255)                        not null comment '菜单名称',
    icon_url    varchar(255)                        null comment '菜单图标',
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

-- auto-generated definition
create table sys_file
(
    id            bigint auto_increment
        primary key,
    file_name     varchar(255)                        not null comment '文件名称',
    file_url      varchar(255)                        not null comment '文件地址',
    md5_name      varchar(255)                        not null comment '文件md5',
    menu_id       bigint                              null comment '菜单id',
    file_size     bigint                              not null comment '文件大小',
    file_type     varchar(255)                        not null comment '文件类型（后缀）',
    business_type tinyint                             null comment '业务类型 eg: 1-点 2-线 3-面',
    create_time   timestamp default CURRENT_TIMESTAMP null,
    update_time   timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create index idx_file_name
    on sys_file (file_name);


