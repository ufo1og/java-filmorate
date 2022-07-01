package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postUser_Default() throws Exception {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        user.setId(responseUser.getId());
        Assertions.assertEquals(user, responseUser);
    }

    @Test
    public void postUser_EmptyRequestBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUser_NegativeId() {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        user.setId(-1);

        NestedServletException e = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(post("/users")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().is5xxServerError())
        );
        String expectedMessage = "Request processing failed; nested exception is javax.validation." +
                "ValidationException: HV000028: Unexpected exception during isValid call.";
        Assertions.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    public void postUser_NullEmail() throws Exception {
        User user = new User(null, "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_BlankEmail() throws Exception {
        User user = new User("", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_BadEmail() throws Exception {
        User user = new User("@usermail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_NullLogin() throws Exception {
        User user = new User("user@mail.ru", null, LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_BlankLogin() throws Exception {
        User user = new User("user@mail.ru", "", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_LoginWithSpace() throws Exception {
        User user = new User("user@mail.ru", "user login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_NullBirthday() throws Exception {
        User user = new User("user@mail.ru", "user_login", null);
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void postUser_BirthdayInFuture() throws Exception {
        User user = new User("user@mail.ru", "user_login", LocalDate.now().plusYears(1));
        user.setName("user_name");

        mvcPerformPostIsBadRequest(user);
    }

    @Test
    public void putUser_JsonWithoutUserIdAndName() throws Exception {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));

        MvcResult result = getMvcResultWithPutMethod(user);

        user.setName(user.getLogin());
        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        user.setId(responseUser.getId());
        Assertions.assertEquals(user, responseUser);
    }

    @Test
    public void putUser_JsonWithUnoccupiedId() throws Exception {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        user.setId(1234567890);

        MvcResult result = getMvcResultWithPutMethod(user);

        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        Assertions.assertEquals(user, responseUser);
    }

    @Test
    public void putUser_putUser_JsonWithOccupiedId() throws Exception {
        User user = new User("user@mail.ru", "user_login", LocalDate.of(2000, 1, 1));
        user.setName("user_name");
        user.setId(12345);
        // Создали нового пользователя с id 12345 и проверили что он создался
        MvcResult result = getMvcResultWithPutMethod(user);
        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        Assertions.assertEquals(user, responseUser);

        user = new User("newuser@mail.ru", "new_user_login", LocalDate.of(2022, 1, 1));
        user.setName("new_user_name");
        user.setId(12345);
        // Перезаписываем пользователя с id 12345 и проверяем
        result = getMvcResultWithPutMethod(user);
        responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        Assertions.assertEquals(user, responseUser);
    }

    @Test
    @Order(1)
    public void getUsers_NoUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<User> responseUsers = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        Assertions.assertEquals(Collections.emptyList(), responseUsers);
    }

    @Test
    @Order(2)
    public void getUsers_Default() throws Exception {
        List<User> users = new ArrayList<>();
        // Генерируем фильмы и добавляем их через PUT запросы
        for (int i = 0; i < 5; i++) {
            User user = new User("user" + i + "@mail.ru", "user_login" + i, LocalDate.of(2000, 1, 1));
            user.setName("user_name" + i);
            user.setId(5000 + i);
            users.add(user);
            getMvcResultWithPutMethod(user);
        }

        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<User> responseUsers = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        Assertions.assertEquals(users, responseUsers);
    }

    private MvcResult getMvcResultWithPutMethod(User user) throws Exception {
        return mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void mvcPerformPostIsBadRequest(User user) throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
