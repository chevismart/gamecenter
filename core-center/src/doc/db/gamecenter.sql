/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/12/24 0:04:24                           */
/*==============================================================*/


drop table if exists access_channel;

drop table if exists aliPay;

drop table if exists center;

drop table if exists centerAndUser;

drop table if exists customer;

drop table if exists customer_wechat;

drop table if exists device;

drop table if exists play_record;

drop table if exists staff;

drop table if exists subscribeTime;

drop table if exists user;

drop table if exists user_channel;

drop table if exists wechat;

drop table if exists wechat_center;

/*==============================================================*/
/* Table: access_channel                                        */
/*==============================================================*/
create table access_channel
(
   channelId            varchar(128) not null,
   channelName          varchar(128),
   primary key (channelId)
);

/*==============================================================*/
/* Table: aliPay                                                */
/*==============================================================*/
create table aliPay
(
   channelId            varchar(128) not null,
   aliId                varchar(256) not null,
   channelName          varchar(128),
   primary key (channelId, aliId)
);

/*==============================================================*/
/* Table: center                                                */
/*==============================================================*/
create table center
(
   centerId             varchar(128) not null,
   centerName           varchar(64),
   extraParam           varchar(1024),
   primary key (centerId)
);

/*==============================================================*/
/* Table: centerAndUser                                         */
/*==============================================================*/
create table centerAndUser
(
   centerId             varchar(128) not null,
   userId               varchar(128) not null,
   primary key (centerId, userId)
);

/*==============================================================*/
/* Table: customer                                              */
/*==============================================================*/
create table customer
(
   userId               varchar(128) not null,
   name                 varchar(128),
   active               bool,
   registerDateTime     timestamp,
   primary key (userId)
);

/*==============================================================*/
/* Table: customer_wechat                                       */
/*==============================================================*/
create table customer_wechat
(
   userId               varchar(128) not null,
   channelId            varchar(128) not null,
   wechatAppId          varchar(128) not null,
   openId               varchar(256),
   primary key (userId, channelId, wechatAppId)
);

/*==============================================================*/
/* Table: device                                                */
/*==============================================================*/
create table device
(
   centerId             varchar(128),
   deviceId             varchar(128),
   deviceName           varchar(64),
   deviceDesc           text,
   macAddr              varchar(64),
   remark               varchar(1024)
);

/*==============================================================*/
/* Table: play_record                                           */
/*==============================================================*/
create table play_record
(
   centerId             varchar(128) not null,
   userId               varchar(128) not null,
   channelId            varchar(128) not null,
   time                 timestamp,
   primary key (centerId, userId, channelId)
);

/*==============================================================*/
/* Table: staff                                                 */
/*==============================================================*/
create table staff
(
   userId               varchar(128) not null,
   name                 varchar(128),
   active               bool,
   staffId              varchar(128),
   primary key (userId)
);

/*==============================================================*/
/* Table: subscribeTime                                         */
/*==============================================================*/
create table subscribeTime
(
   userId               varchar(128) not null,
   channelId            varchar(128) not null,
   wechatAppId          varchar(128) not null,
   subscribeTime        timestamp,
   primary key (userId, channelId, wechatAppId)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   userId               varchar(128) not null,
   name                 varchar(128),
   active               bool,
   primary key (userId)
);

/*==============================================================*/
/* Table: user_channel                                          */
/*==============================================================*/
create table user_channel
(
   userId               varchar(128) not null,
   channelId            varchar(128) not null,
   primary key (userId, channelId)
);

/*==============================================================*/
/* Table: wechat                                                */
/*==============================================================*/
create table wechat
(
   channelId            varchar(128) not null,
   wechatAppId          varchar(128) not null,
   channelName          varchar(128),
   wechatAppName        varchar(512),
   primary key (channelId, wechatAppId)
);

/*==============================================================*/
/* Table: wechat_center                                         */
/*==============================================================*/
create table wechat_center
(
   centerId             varchar(128) not null,
   channelId            varchar(128) not null,
   wec_wechatAppId      varchar(128) not null,
   wechatAppId          varchar(128) not null,
   appSecret            varchar(64) not null,
   primary key (centerId, channelId, wec_wechatAppId)
);

alter table aliPay add constraint FK_aliPayIsChannel foreign key (channelId)
      references access_channel (channelId) on delete restrict on update restrict;

alter table centerAndUser add constraint FK_centerAndUser foreign key (centerId)
      references center (centerId) on delete restrict on update restrict;

alter table centerAndUser add constraint FK_centerAndUser2 foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table customer add constraint FK_customerIsUser foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table customer_wechat add constraint FK_customer_wechat foreign key (userId)
      references customer (userId) on delete restrict on update restrict;

alter table customer_wechat add constraint FK_customer_wechat2 foreign key (channelId, wechatAppId)
      references wechat (channelId, wechatAppId) on delete restrict on update restrict;

alter table device add constraint FK_Relationship_3 foreign key (centerId)
      references center (centerId) on delete restrict on update restrict;

alter table play_record add constraint FK_play_record foreign key (centerId)
      references center (centerId) on delete restrict on update restrict;

alter table play_record add constraint FK_play_record2 foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table play_record add constraint FK_play_record3 foreign key (channelId)
      references access_channel (channelId) on delete restrict on update restrict;

alter table staff add constraint FK_staffIsUser foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table subscribeTime add constraint FK_subscribeTime foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table subscribeTime add constraint FK_subscribeTime2 foreign key (channelId, wechatAppId)
      references wechat (channelId, wechatAppId) on delete restrict on update restrict;

alter table user_channel add constraint FK_user_channel foreign key (userId)
      references user (userId) on delete restrict on update restrict;

alter table user_channel add constraint FK_user_channel2 foreign key (channelId)
      references access_channel (channelId) on delete restrict on update restrict;

alter table wechat add constraint FK_wechatIsChannel foreign key (channelId)
      references access_channel (channelId) on delete restrict on update restrict;

alter table wechat_center add constraint FK_wechat_center foreign key (centerId)
      references center (centerId) on delete restrict on update restrict;

alter table wechat_center add constraint FK_wechat_center2 foreign key (channelId, wec_wechatAppId)
      references wechat (channelId, wechatAppId) on delete restrict on update restrict;

