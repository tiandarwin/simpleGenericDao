create database demo;

use demo;

create table IF NOT EXISTS comments_0(
  `id` bigint unsigned not null AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(64) not null DEFAULT '',
  `comments` varchar(255) not null DEFAULT '',
  `user_id` bigint(20) not null DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

create table IF NOT EXISTS comments_1(
  `id` bigint unsigned not null AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(64) not null DEFAULT '',
  `comments` varchar(255) not null DEFAULT '',
  `user_id` bigint(20) not null DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

create table IF NOT EXISTS comments_2(
  `id` bigint unsigned not null AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(64) not null DEFAULT '',
  `comments` varchar(255) not null DEFAULT '',
  `user_id` bigint(20) not null DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

create table IF NOT EXISTS comments_3(
  `id` bigint unsigned not null AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(64) not null DEFAULT '',
  `comments` varchar(255) not null DEFAULT '',
  `user_id` bigint(20) not null DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;