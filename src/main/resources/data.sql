MERGE INTO FRIENDSHIP_STATUS(status_id, status_name) values ( 0, 'CONFIRMED' ),(1, 'UNCONFIRMED');

MERGE INTO GENRES(genre_id, genre_name)
VALUES ( 1, 'COMEDY' ),
       (2, 'DRAMA'),
       (3, 'CARTOON'),
       (4, 'THRILLER'),
       (5, 'DOCUMENTARY'),
       (6, 'ACTION');

MERGE INTO RATINGS(rating_id, rating_name)
VALUES ( 1, 'G' ),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');