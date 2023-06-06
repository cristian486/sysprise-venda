create table venda(
    id bigint not null auto_increment,
    documento varchar(200) not null unique,
    observacao varchar(500),
    data_de_criacao timestamp,
    data_de_entrega date,
    status varchar(30) not null,
    desconto numeric(5, 2),
    habilitado boolean,
    cliente_id bigint,
    constraint vendapk primary key(id)
);

create table item_venda(
    id bigint not null auto_increment,
    venda_id bigint,
    produto_id bigint,
    preco_unitario numeric(8, 3),
    quantidade numeric(8, 3),
    constraint item_vendapk primary key(id),
    constraint item_venda_vendafk foreign key(venda_id) references venda(id)
);