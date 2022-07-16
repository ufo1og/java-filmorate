package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserStorage storage;

    @AfterEach
    private void clearStorage() {
        storage.deleteAll();
    }

    @Test
    public void whenCreatedUser_thenStatus200() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        user.setId(1);
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void whenCreatingNotValidUser_thenStatus400() {
        // blank login
        User user = new User("user@mail.ru", "", LocalDate.of(2000, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // null login
        user = new User("user@mail.ru", null, LocalDate.of(2000, 1, 1));
        response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // blank email
        user = new User("", "user_login", LocalDate.of(2000, 1, 1));
        response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // null email
        user = new User(null, "user_login", LocalDate.of(2000, 1, 1));
        response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // bad email
        user = new User("user@mail.ru!", "user_login", LocalDate.of(2000, 1, 1));
        response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // bad birthday
        user = new User("user@mail.ru", "user_login", LocalDate.now().plusYears(5));
        response = restTemplate.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdatedUser_thenStatus200() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        storage.create(user);
        user = new User("new_user@mail.ru", "new_user_login", LocalDate.of(2000, 1, 1));
        user.setName("new_user_name");
        user.setId(1);
        HttpEntity<User> entity = new HttpEntity<>(user);

        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void whenUpdatingUserWithNegativeId_thenStatus404() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setId(-1);
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenUpdatingUserThatNotExist_thenStatus404() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setId(777);
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<User> response = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenGetUsers_thenStatus200() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        storage.create(user);
        user.setId(1);
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(List.of(user), response.getBody());
    }

    @Test
    public void whenGetUser_thenStatus200() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        storage.create(user);
        user.setId(1);
        ResponseEntity<User> response = restTemplate.exchange("/users/{id}", HttpMethod.GET, null, User.class, 1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void whenGetUserThatNotExist_thenStatus404() {
        ResponseEntity<User> response = restTemplate.exchange("/films/{id}", HttpMethod.GET, null, User.class, 777);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
