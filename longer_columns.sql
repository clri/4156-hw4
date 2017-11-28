use db_example;
alter table review modify column review_body varchar(1500);
alter table job modify column jobdesc varchar(1500);

use db_test;
alter table review modify column review_body varchar(1500);
alter table job modify column jobdesc varchar(1500);
