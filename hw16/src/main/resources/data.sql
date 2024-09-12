insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1),
       ('BookTitle_2', 2, 2),
       ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('good book', 1),
       ('real good book', 1),
       ('real good1 book', 2),
       ('real good2 book', 2),
       ('bad book', 3);

insert into users(username, password)
values ('user', '$2a$10$.upwcs0MTqwvala0rSxEaO2rLkXKOu1ktGIPR4C5M0pxxhjeUMmby'),
       ('user1', '$2a$10$.upwcs0MTqwvala0rSxEaO2rLkXKOu1ktGIPR4C5M0pxxhjeUMmby'); /*root*/


insert into user_authorities(user_id, authority)
values (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');
