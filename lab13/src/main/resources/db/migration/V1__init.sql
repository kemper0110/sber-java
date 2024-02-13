create table users
(
    id        bigserial primary key,
    firstname varchar(50),
    lastname  varchar(50),
    job       varchar(50),
    mail      varchar(50),
    phone     varchar(50),
    image     varchar(50)
);