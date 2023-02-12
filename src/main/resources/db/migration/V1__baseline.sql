use apisample;

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
    id         bigint       not null auto_increment,
    login      varchar(100) not null,
    senha      varchar(255) not null,
    token      varchar(255),
    last_access datetime,
    primary key (id)

);
-- user: admin / senha: 'teste'
insert into usuarios(login, senha) values ('admin', '$2a$12$.Fnc0yYaIv6ZPNMmgaz5NO8xTTFWYcbnsI2.fBAxO49G4lSsnc6mi');

insert into cadastros(nome, email, telefone, ativo) values ('Victor Ferraz Pimentel', 'vfpimentel@gmail.com', '62984369747', true);
insert into cadastros(nome, email, telefone, ativo) values ('Cadastro Teste', 'cadastro@teste.com', '6262626262', true);