Postgres Installation
--

brew install postgresql 

—Start server with bellow command
pg_ctl -D /usr/local/var/postgres start

Install PgAmdin4


pg_ctl -D /usr/local/var/postgres stop


Command:
psql postgres

create database approvals;
create user appuser with encrypted password 'Test@123';