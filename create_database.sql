#note: This file assumes you are using mysql/mariadb with innodb tables enabled
#first you need to have the database created, and the user credentials set up

CREATE DATABASE IF NOT EXISTS lemon_db;
USE lemon_db;
create user 'le-mon'@'localhost' identified by '1EM4Gn0FDwnTEklAZQ6m';
grant all privileges on lemon_db.* to 'le-mon'@'localhost';

#and below are the sql statements to create the database

alter table bm_description drop foreign key FK78D2D608B6C1CC8C;
alter table bm_value drop foreign key FKF3035C7D8D6A0CB8;
alter table probe_description drop foreign key FKB823A1ED8D6A0CB8;
alter table probe_description drop foreign key FKB823A1EDB6C1CC8C;

DROP TABLE IF EXISTS lemon_db.bm_description;
DROP TABLE IF EXISTS lemon_db.bm_value;
DROP TABLE IF EXISTS lemon_db.event;
DROP TABLE IF EXISTS lemon_db.probe_description;
DROP TABLE IF EXISTS lemon_db.target_description;

CREATE TABLE lemon_db.bm_description (
  bm_id bigint(20) NOT NULL auto_increment,
  measure_uri varchar(255),
  data_type int(4),
  description varchar(255),
  bm_name varchar(255),
  target_id bigint(20),
  PRIMARY KEY (bm_id)) ENGINE=InnoDB;

CREATE TABLE lemon_db.bm_value (
  value_id bigint(20) NOT NULL auto_increment,
  value_precision int(11),
  value_time datetime,
  value_string varchar(255),
  bm_id bigint(20),
  PRIMARY KEY (value_id)) ENGINE=InnoDB;

CREATE TABLE lemon_db.event (
  event_id bigint(20) NOT NULL auto_increment,
  message varchar(255),
  event_source varchar(255),
  event_time datetime,
  event_type int(4),
  PRIMARY KEY (event_id)
) ENGINE=InnoDB;

CREATE TABLE  lemon_db.probe_description (
  probe_id bigint(20) NOT NULL auto_increment,
  endpoint varchar(255),
  bm_precision int(11),
  probe_name varchar(255),
  bm_id bigint(20),
  target_id bigint(20),
  PRIMARY KEY (probe_id)) ENGINE=InnoDB;

CREATE TABLE  lemon_db.target_description (
  target_id bigint(20) NOT NULL auto_increment,
  target_name varchar(255),
  target_type varchar(255),
  PRIMARY KEY (target_id)) ENGINE=InnoDB;

alter table bm_description add index FK78D2D608B6C1CC8C (target_id), add constraint FK78D2D608B6C1CC8C foreign key (target_id) references target_description (target_id);
alter table bm_value add index FKF3035C7D8D6A0CB8 (bm_id), add constraint FKF3035C7D8D6A0CB8 foreign key (bm_id) references bm_description (bm_id);
alter table probe_description add index FKB823A1ED8D6A0CB8 (bm_id), add constraint FKB823A1ED8D6A0CB8 foreign key (bm_id) references bm_description (bm_id);
alter table probe_description add index FKB823A1EDB6C1CC8C (target_id), add constraint FKB823A1EDB6C1CC8C foreign key (target_id) references target_description (target_id);

