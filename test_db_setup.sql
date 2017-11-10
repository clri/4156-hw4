/*RUN THIS IN THE SAME DIRECTORY AS THE test_db.sql FILE*/
create database db_test;
use db_test;
source test_db.sql;
create user test_u@localhost identified by 'ThePassword';
grant all on db_test.* to 'test_u'@'localhost';
revoke all on db_test.* from 'test_u'@'localhost';
grant select, insert, delete, update on db_test.* to 'test_u'@'localhost';


