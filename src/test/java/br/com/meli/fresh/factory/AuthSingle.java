package br.com.meli.fresh.factory;

import br.com.meli.fresh.dto.request.AuthRequest;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public final class AuthSingle {

    private UserServiceImpl userService;
    public static String TOKEN;
    private final static String PASSWORD = "123";
    public static String USER_ID;
    private static AuthSingle INSTANCE;

    public AuthSingle(UserServiceImpl userService, MockMvc mockMvc, User user) {
        this.userService = userService;
        this.TOKEN = this.token(user, mockMvc);
    }

    public static AuthSingle getInstance(UserServiceImpl userService, MockMvc mockMvc) {
        if(INSTANCE == null) {
            User user = new User(
                    null,
                    "admin",
                    "admin@admin.com",
                    PASSWORD,
                    Set.of(0, 1, 2, 3)
            );

            INSTANCE = new AuthSingle(userService, mockMvc, user);
        }

        return INSTANCE;
    }

    public static AuthSingle getInstance(UserServiceImpl userService, MockMvc mockMvc, User user) {
        if(INSTANCE == null) {
            user.setPassword(PASSWORD);
            INSTANCE = new AuthSingle(userService, mockMvc, user);
        }

        return INSTANCE;
    }

    private String token(User user, MockMvc mockMvc) {
        if(TOKEN == null) {
            User u = userService.create(user);
            USER_ID = u.getId();
            String result = login(u, mockMvc);
            return result;
        }
        return this.TOKEN;
    }

    private static String login(User user, MockMvc mockMvc) {
        // Setting payload login's request
        AuthRequest auth = new AuthRequest();
        auth.setEmail(user.getEmail());
        auth.setPassword(PASSWORD);

        try {

            ObjectWriter writer = new ObjectMapper()
                    .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                    .writer().withDefaultPrettyPrinter();

            String payloadLogin = writer.writeValueAsString(auth);

            // Realizing auth this user.
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadLogin))
                    .andExpect(status().isOk()).andReturn();
            String result = mvcResult.getResponse().getHeader("Authorization");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}