--changeset l.frolenkov:EXS-00 context:master
--comment: Create table users
create sequence s_users start with 1 increment by 1;

create table users
(
    id bigint not null,
    username varchar(256) unique not null,
    password character varying not null,

    constraint pk_users primary key (id)
);
--rollback drop sequence s_users;
--rollback drop table users;

