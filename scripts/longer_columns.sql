use db_example;
alter table review modify column review_body varchar(1500);
alter table job modify column jobdesc varchar(1500);
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
alter table review modify column review_body varchar(1500);
alter table job modify column jobdesc varchar(1500);
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
