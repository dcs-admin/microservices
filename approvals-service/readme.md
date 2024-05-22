

# How to Start and test this serice

1. Start as springbot app
```
mvn spring-boot:run 
```

Then It will register as microservice 
Start Debezium connector to mysql 

NOTE: Start Postgress sql also 

2. MYSQL Part 
Open Sequel Pro and try to run these
```
CREATE TABLE `itil_approvals_custom` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `approval_status` int(11) DEFAULT NULL,
  `approvable_id` bigint(20) DEFAULT NULL,
  `approvable_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `token` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `remark` text COLLATE utf8_unicode_ci,
  `last_reminded_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `triggered_at` datetime DEFAULT NULL,
  `delegatee_id` bigint(20) DEFAULT NULL,
  `level_id` smallint(6) DEFAULT '1',
  `approval_type` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
   
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



INSERT INTO itil_approvals_custom(user_id, member_id, account_id, approval_status, approvable_id,approvable_type, deleted, remark, triggered_at, delegatee_id, approval_type, level_id) values (1, 1, 1, 1, 100,'APPR', 0, 'Simplessdsdsd', now(), 0, 10, 2);

```

3. Once INSERT above, then you will see an CDC event in Approvals service

```
Received event: Struct{id=10}
                 after: Struct{id=10,user_id=1,member_id=1,account_id=1,approval_status=1,approvable_id=100,approvable_type=APPR,deleted=0,remark=Simplessdsdsd,triggered_at=1713287470000,delegatee_id=0,level_id=2,approval_type=10}
```

4. POSTGRESSQL

Open PgAmdin and see tables 
```
—Start server with bellow command
pg_ctl -D /usr/local/var/postgres start

Install PgAmdin4


pg_ctl -D /usr/local/var/postgres stop
create database approvals;
create user appuser with encrypted password 'Test@123';

```