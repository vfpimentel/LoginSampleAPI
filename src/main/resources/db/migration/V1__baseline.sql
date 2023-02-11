create table cadastros
(
    id       bigint       not null auto_increment,
    nome     varchar(100) not null,
    email    varchar(100) not null unique,
    telefone varchar(20)  not null,
    ativo    tinyint default 1,
    primary key (id)
);

create table usuarios
(

    id    bigint       not null auto_increment,
    login varchar(100) not null,
    senha varchar(255) not null,

    primary key (id)

);