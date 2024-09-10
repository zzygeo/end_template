-- 创建库
create database if not exists end_template character set utf8mb4 collate utf8mb4_unicode_ci;

-- 切换库
use end_template;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';


-- auto-generated definition
create table oper_log
(
    id           bigint auto_increment
        primary key,
    title        varchar(50)  default ''                null comment '模块标题',
    businessType char         default '0'               null comment '业务类型（0其他 1新增 2修改 3删除）',
    operMethod   varchar(100) default ''                null comment '方法名称',
    operName     varchar(50)  default ''                null comment '操作人员',
    operUrl      varchar(255) default ''                null comment '请求URL',
    operIp       varchar(128) default ''                null comment '请求ip',
    operParam    text                                   null comment '请求参数',
    jsonResult   text                                   null comment '返回参数',
    status       char         default '0'               null comment '操作状态（0正常 1异常）',
    errorMsg     text                                   null comment '错误信息',
    operTime     datetime    default CURRENT_TIMESTAMP not null comment '操作时间',
    costTime     bigint       default 0                 null comment '消耗时间'
)
    comment '日志操作表';

create index idx_businessType
    on oper_log (businessType);

create index idx_operTime
    on oper_log (operTime);

-- auto-generated definition
create table login_info
(
    id            bigint auto_increment
        primary key,
    userName      varchar(50)  default ''                null comment '用户名',
    ipAddr        varchar(128) default ''                null comment 'ip地址',
    loginLocation varchar(255) default ''                null comment '登陆地址',
    browser       varchar(50)  default ''                null comment '浏览器信息',
    os            varchar(50)  default ''                null comment '系统信息',
    status        char         default '0'               null comment '登陆状态',
    msg           varchar(255) default '提示消息'        null,
    loginTime     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '访问时间'
) comment '登陆日志表';

create index idx_loginTime
    on login_info (loginTime);



