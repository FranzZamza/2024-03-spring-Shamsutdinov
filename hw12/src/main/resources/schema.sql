create table authors (
    id bigserial,
    full_name varchar(255) unique,
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255) unique,
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255) unique,
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table comments(
    id bigserial,
    text varchar(1024),
    book_id bigint references books (id),
    primary key (id)
);

create table users(
    id bigserial,
    username varchar(128),
    password varchar (128),
    role varchar(20),
    primary key (id)
);