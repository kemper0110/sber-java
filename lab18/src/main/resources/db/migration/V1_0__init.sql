create table receipt
(
    id   bigserial primary key,
    name varchar(50) not null
);

create table ingredient
(
    id   bigserial primary key,
    name varchar(50) not null
);

create table receipt_ingredient
(
    receipt_id    bigint references receipt (id) on delete CASCADE    not null,
    ingredient_id bigint references ingredient (id) on delete CASCADE not null,
    quantity      smallint                                            not null,
    check ( quantity != 0 ),
    constraint receipt_ingredient_id PRIMARY KEY (receipt_id, ingredient_id)
);