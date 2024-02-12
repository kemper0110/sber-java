create table request
(
    id   bigserial primary key,
    text text
);

create table response
(
    id   bigserial primary key references request(id) on UPDATE cascade on delete cascade,
    text text
)