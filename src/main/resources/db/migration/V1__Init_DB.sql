create sequence bucket_seq start 1 increment 1
create sequence category_seq start 1 increment 1
create sequence order_details_seq start 1 increment 1
create sequence order_seq start 1 increment 1
create sequence product_seq start 1 increment 1
create sequence user_seq start 1 increment 1

create table bucket_products_tbl (bucket_id int8 not null, product_id int8 not null);

create table buckets_tbl (id int8 not null, user_id int8, primary key (id));

create table categories_tbl (id int8 not null, title varchar(255), primary key (id));

create table order_details_tbl (
    id int8 not null,
    amount numeric(19, 2),
    price numeric(19, 2),
    order_id int8,
    product_id int8,
    primary key (id)
    );

create table orders_tbl (
    id int8 not null,
    address varchar(255),
    changed timestamp,
    created timestamp,
    status varchar(255),
    sum numeric(19, 2),
    user_id int8,
    primary key (id)
    );

create table product_categories_tbl (product_id int8 not null, category_id int8 not null);

create table products_tbl (id int8 not null, price float8, title varchar(255), primary key (id));

create table users_tbl (
    id int8 not null,
    archive boolean not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    role varchar(255),
    bucket_id int8,
    primary key (id)
    );

alter table if exists bucket_products
    add constraint FKpr1k7tk7i0orlefrsvc6oypwy
        foreign key (product_id) references products_tbl;

alter table if exists bucket_products
    add constraint FKejswpho61ms1v5eselgw5p2nj
        foreign key (bucket_id) references buckets_tbl;

alter table if exists buckets_tbl
    add constraint FK5fmxd234trk46gchv7xju7q8v
        foreign key (user_id) references users_tbl;

alter table if exists order_details_tbl
    add constraint FKfwnnigadhgbh3bh385822gwpu
        foreign key (order_id) references orders_tbl;

alter table if exists order_details_tbl
    add constraint FKtjcy582393gb2wdq0w7lurrbe
        foreign key (product_id) references products_tbl;

alter table if exists orders_tbl
    add constraint FKciwqueax6a5o9rvk902udaid5
        foreign key (user_id) references users_tbl;

alter table if exists product_categories_tbl
    add constraint FKa7w660fwjy06u3ph2ndwvh9u0
        foreign key (category_id) references categories_tbl;

alter table if exists product_categories_tbl
    add constraint FKeg6p7g3chbonvfq9w3rp5eh9c
        foreign key (product_id) references products_tbl;

alter table if exists users_tbl
    add constraint FKhkc5rquiikhfmp2koowtvmrbr
        foreign key (bucket_id) references buckets_tbl;