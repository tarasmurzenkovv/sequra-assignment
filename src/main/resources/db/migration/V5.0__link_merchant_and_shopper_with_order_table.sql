alter table "order" add constraint "merchant_id" foreign key (merchant_id) references merchant (id);
alter table "order" add constraint "shopper_id" foreign key (shopper_id) references shopper (id);