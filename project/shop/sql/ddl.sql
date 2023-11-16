create table item (

item_id Long,
item_name varchar(50) not null,
price number not null,
stock_number number not null,
item_detail CLOB not null,
item_sell_status varchar2(255),
reg_time date,
update_time date
);
ALTER TABLE item
ADD CONSTRAINT item_id_pk PRIMARY KEY(itme_id);