----------------------------- H2 -----------------------------
-- runscript from 'db_connect.sql';
show tables;
show columns from MyEntity1;
select count(*) from MyEntity1;

----------------------------- DERBY -----------------------------
-- run 'db_connect.sql';
-- connect 'jdbc:derby://localhost:1527/minibank';
-- show connections;
-- show tables;
-- describe MyEntity1;
-- show indexes from MyEntity1;
-- select * from syscs_diag.lock_table;
-- select count(*) from MyEntity1;

