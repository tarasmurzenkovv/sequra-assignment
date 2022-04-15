CREATE table if not exists merchant (
    id serial primary key,
    name varchar(50) not null,
    email varchar not null,
    cif varchar not null
)