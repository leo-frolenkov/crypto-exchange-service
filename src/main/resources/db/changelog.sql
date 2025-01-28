-- liquibase formatted sql
--changeset l.frolenkov:EXS-00 context:master
--comment: Create table users
create sequence if not exists s_users start with 1 increment by 1;

create table if not exists users
(
    id bigint not null,
    username varchar(256) unique not null,
    password character varying not null,

    constraint pk_users primary key (id)
);

create index if not exists username_users_idx on users(username);
--rollback drop sequence s_users;
--rollback drop table users;
--rollback drop index username_users_idx;

--changeset l.frolenkov:EXS-01 context:master
--comment: Create table roles
create sequence if not exists s_roles start with 1 increment by 1;

create table if not exists roles
(
    id bigint not null ,
    name varchar(256) not null unique ,
    description varchar(1000),

    constraint pk_roles primary key (id)
);

create index if not exists name_roles_idx on roles(name);
--rollback drop sequence s_roles;
--rollback drop table roles;
--rollback drop index name_roles_idx;

--changeset l.frolenkov:EXS-03 context:master
--comment: Insert basic roles
insert into roles(id, name, description)
values (nextval('s_roles'), 'ADMIN', 'admin role'),
       (nextval('s_roles'), 'CLIENT', 'client');
--rollback TRUNCATE table roles;

--changeset l.frolenkov:EXS-04 context:master
--comment: create table user_to_roles
create sequence s_user_to_roles start with 1 increment by 1;

create table if not exists user_to_roles
(
    id bigint not null,
    user_id bigint not null,
    role_id bigint not null,

    constraint pk_user_to_roles primary key (id),
    constraint fk_user_to_roles_user_id foreign key (user_id) references users(id),
    constraint fk_user_to_roles_role_id foreign key (role_id) references roles(id)
);

create unique index if not exists user_to_roles_user_role_uidx on user_to_roles(user_id, role_id);
--rollback drop sequence s_user_to_roles;
--rollback drop table user_to_roles;
--rollback drop index user_roles_user_roles_uidx;

--changeset l.frolenkov:EXS-05 context:master
--comment: create table for wallet
create sequence if not exists s_wallets start with 1 increment by 1;

create table if not exists wallets
(
    id bigint not null ,
    user_id bigint not null,
    currency varchar(20) not null,
    type varchar(20) not null,
    balance numeric(19,2) not null default 0,

    constraint pk_wallet primary key (id)
);

create index if not exists wallets_user_id_idx on wallets(user_id);
--rollback drop sequence s_wallets;
--rollback drop table wallets;
--rollback drop index wallets_user_id_idx;

--changeset l.frolenkov:EXS-06 context:master
--comment: create order table;
create sequence if not exists s_orders start with 1 increment by 1;

create table if not exists orders
(
    id bigint not null,
    user_id bigint not null,
    currency varchar(20) not null,
    amount numeric(19,2) not null default 0,
    price numeric(19,2) not null,
    type varchar(20) not null,
    order_kind varchar(20) not null,
    status varchar(20) not null,
    created_at timestamp with time zone default now(),

    constraint pk_orders primary key (id),
    constraint fk_orders_user_id foreign key (user_id) references users(id)
);

create index if not exists orders_currency_idx on orders(currency);
create index if not exists orders_user_id_idx on orders(user_id);
--rollback drop sequence s_orders;
--rollback drop table orders;
--rollback drop index orders_currency_idx;
--rollback drop index orders_user_id_idx;