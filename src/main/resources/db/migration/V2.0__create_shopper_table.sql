CREATE table if not exists shopper (
    id serial primary key,
    name varchar(50) not null,
    email varchar not null,
    nif varchar not null
)