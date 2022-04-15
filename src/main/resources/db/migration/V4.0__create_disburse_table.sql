CREATE table if not exists disburse (
    id serial primary key,
    order_id int,
    amount decimal not null
)