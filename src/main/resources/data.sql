MERGE INTO FRIENDSHIP_STATUS(status_id, status_name)
    values (0, 'UNCONFIRMED'),(1, 'CONFIRMED');

MERGE INTO GENRES(genre_id, genre_name)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

MERGE INTO MPA(mpa_id, mpa_name)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');