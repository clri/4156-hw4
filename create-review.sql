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
