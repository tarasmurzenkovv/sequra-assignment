insert into disburse (order_id, amount)
select o.id, 200
from "order" as o
         join merchant on o.merchant_id = merchant.id
where completed_at is not null;