create database db_example;
create user 'springuser'@'localhost' identified by 'ThePassword';
grant all on db_example.* to 'springuser'@'localhost';


