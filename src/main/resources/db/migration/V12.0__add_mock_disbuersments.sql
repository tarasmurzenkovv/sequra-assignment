insert into "order" (merchant_id, shopper_id, amount, created_at, completed_at)
values
    ((select id from merchant where name='Treutel, Schumm and Fadel'),
     (select id from shopper where name='Olive Thompson'),
     '100', now(), null),
    ((select id from merchant where name='Treutel, Schumm and Fadel'),
     (select id from shopper where name='Olive Thompson'),
     '200', now(), null),
((select id from merchant where name='Treutel, Schumm and Fadel'),
    (select id from shopper where name='Olive Thompson'),
    '300', now(), now()),
    ((select id from merchant where name='Treutel, Schumm and Fadel'),
     (select id from shopper where name='Olive Thompson'),
     '400', now(), now())
;