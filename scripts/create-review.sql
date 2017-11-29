use db_example;
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee` int(11) DEFAULT NULL,
  `employer` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `review_body` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `reset_token`;
create table Reset_Token (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `user_email` varchar(255) DEFAULT NULL,
        `token` varchar(255) DEFAULT NULL,
        PRIMARY KEY (`id`),
        constraint unique(user_email)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table request
add column employer_read boolean,
add column employee_read boolean,
add column request_time datetime,
add column decision_time datetime,
add column decision int(1);

delimiter $$
drop trigger if exists req_update $$
create trigger req_update before update on request
for each ROW
BEGIN
        declare noe int;
        if new.decision = 1 and old.decision != 1 then
                select count(*) into noe from request r where r.job = new.job and new.decision = 1;
                if (noe is not null and noe > 0) then
                        signal sqlstate '12345';
                end if;
        end if;
end $$
delimiter ;

use db_test;
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee` int(11) DEFAULT NULL,
  `employer` int(11) DEFAULT NULL,
  `job` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `review_body` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

drop table if exists `reset_token`;
create table Reset_Token (
        `id` int(11) NOT NULL AUTO_INCREMENT,
        `user_email` varchar(255) DEFAULT NULL,
        `token` varchar(255) DEFAULT NULL,
        PRIMARY KEY (`id`),
        constraint unique(user_email)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table request
add column employer_read boolean,
add column employee_read boolean,
add column request_time datetime,
add column decision_time datetime,
add column decision int(1);

delimiter $$
drop trigger if exists req_update $$
create trigger req_update before update on request
for each ROW
BEGIN
        declare noe int;
        if new.decision = 1 and old.decision != 1 then
                select count(*) into noe from request r where r.job = new.job and new.decision = 1;
                if (noe is not null and noe > 0) then
                        signal sqlstate '12345';
                end if;
        end if;
end $$
delimiter ;
