create table IF NOT EXISTS FILMS
(
    FILM_ID      BIGINT auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER LARGE OBJECT,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATING       CHARACTER VARYING(5)   not null,
    constraint FILM_ID_PK
        primary key (FILM_ID)
);

create table IF NOT EXISTS FILMS_GENRES
(
    FILM_ID BIGINT                not null,
    GENRE   CHARACTER VARYING(20) not null,
    constraint FILMS_GENRES_PK
        primary key (FILM_ID, GENRE),
    constraint FILM_ID_GENRE_FK
        foreign key (FILM_ID) references FILMS
);

create table IF NOT EXISTS USERS
(
    USER_ID   BIGINT auto_increment,
    EMAIL     CHARACTER VARYING(200) not null,
    LOGIN     CHARACTER VARYING(50)  not null,
    USER_NAME CHARACTER VARYING(200),
    BIRTHDAY  DATE,
    constraint USER_ID_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FILMS_LIKES
(
    FILM_ID BIGINT not null,
    USER_ID BIGINT not null,
    constraint FILMS_LIKES_PK
        primary key (FILM_ID, USER_ID),
    constraint FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FRIENDS
(
    USER_FROM BIGINT not null,
    USER_TO   BIGINT not null,
    STATUS    CHARACTER VARYING(11),
    constraint FRIENDS_KEY
        primary key (USER_FROM, USER_TO),
    constraint USER_FROM_FK
        foreign key (USER_FROM) references USERS,
    constraint USER_TO_FK
        foreign key (USER_TO) references USERS
);

create unique index IF NOT EXISTS USER_EMAIL_UNIQUE
    on USERS (EMAIL);

create unique index IF NOT EXISTS USER_LOGIN_UNIQUE
    on USERS (LOGIN);

