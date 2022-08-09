MERGE INTO FRIENDSHIP_STATUS(status_id, status_name)
    values (0, 'UNCONFIRMED'),(1, 'CONFIRMED');

MERGE INTO GENRES(genre_id, genre_name)
    VALUES (1, 'COMEDY'),
    (2, 'DRAMA'),
    (3, 'CARTOON'),
    (4, 'THRILLER'),
    (5, 'DOCUMENTARY'),
    (6, 'ACTION');

MERGE INTO MPA(mpa_id, mpa_name)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

-- для тестирования
-- MERGE INTO USERS(user_id, email, login, user_name, birthday)
--     VALUES (1, 'user1@mail.ru', 'user1', 'user_name1', '1946-08-20'),
--            (2, 'user2@mail.ru', 'user2', 'user_name2', '1946-08-20'),
--            (3, 'user3@mail.ru', 'user3', 'user_name3', '1946-08-20'),
--            (4, 'user4@mail.ru', 'user4', 'user_name4', '1946-08-20'),
--            (5, 'user5@mail.ru', 'user5', 'user_name5', '1946-08-20'),
--            (6, 'user6@mail.ru', 'user6', 'user_name6', '1946-08-20');

-- MERGE INTO FRIENDS(user_from, user_to, STATUS_ID)
--     VALUES (1, 2, 1),
--            (1, 3, 1),
--            (2, 4, 1),
--            (1, 5, 0),
--            (3, 2, 1),
--            (4, 1, 1),
--            (5, 2, 0);