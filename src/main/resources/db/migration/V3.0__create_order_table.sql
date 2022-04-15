CREATE table if not exists "order" (
    id serial primary key,
    merchant_id int not null,
    shopper_id int not null,
    amount decimal not null ,
    created_at timestamp not null,
    completed_at timestamp
)