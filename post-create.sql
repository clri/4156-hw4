revoke all on db_example.* from 'springuser'@'localhost';
grant select, insert, delete, update on db_example.* to 'springuser'@'localhost';

create view user_roles as
	select 
	id as user_role_id,
	id as username,
	'ROLE_USER' as role
	from user;


