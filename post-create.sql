revoke all on db_example.* from 'springuser'@'localhost';
grant select, insert, delete, update on db_example.* to 'springuser'@'localhost';

create view db_example.user_roles as
	select 
	id as user_role_id,
	id as username,
	'ROLE_USER' as role
	from db_example.user;
alter table user add unique('user_email');

