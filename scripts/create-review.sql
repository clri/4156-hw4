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