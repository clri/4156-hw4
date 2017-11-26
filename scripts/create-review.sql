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

select id, employee, employer, job, msg, title, employer_read, employee_read, request_time, decision_time, decision from
(select r.*, r.decision_time as ti from Request r
where r.employee = 1
and not r.employee_read
and r.decision > 0
Union
select rr.*, rr.request_time as ti from Request rr
where rr.employer = 1
and not rr.employer_read
and rr.decision = 0) R3
order by ti
