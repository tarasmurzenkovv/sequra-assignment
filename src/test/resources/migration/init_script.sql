CREATE table if not exists merchant
(
    id    serial primary key,
    name  varchar(50) not null,
    email varchar     not null,
    cif   varchar     not null
);
CREATE table if not exists shopper
(
    id    serial primary key,
    name  varchar(50) not null,
    email varchar     not null,
    nif   varchar     not null
);
CREATE table if not exists "order"
(
    id           serial primary key,
    merchant_id  int       not null,
    shopper_id   int       not null,
    amount       decimal   not null,
    created_at   timestamp not null,
    completed_at timestamp
);
CREATE table if not exists disburse
(
    id       serial primary key,
    order_id int,
    amount   decimal not null
);
alter table "order" add constraint "merchant_id" foreign key (merchant_id) references merchant (id);
alter table "order" add constraint "shopper_id" foreign key (shopper_id) references shopper (id);
alter table disburse add constraint "order_id" foreign key (order_id) references "order" (id);
alter table merchant add constraint "unique_merchant_email" unique (email);
alter table shopper add constraint "unique_shopper_email" unique (email);
create sequence hibernate_sequence;