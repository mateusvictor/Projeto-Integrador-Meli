package br.com.meli.fresh.factory;

import br.com.meli.fresh.dto.request.AuthRequest;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuthFactory {

    @Autowired
    private UserServiceImpl service;
    private String TOKEN;

    public AuthFactory(UserServiceImpl userService) {
        this.service = userService;
    }


    private User adminUser = null;
    private User userB = null;
    private final String defaultPassword = "admin";

    public User getAdminUser(){
        // Creates only one admin user to be used in the requests
        if (adminUser == null)
            adminUser = service.create(new User(
                null,
                "admin",
                "admin@admin.com",
                "admin",
                Set.of(0, 1, 2, 3)
            ));
        return adminUser;
    }

    public User getNonAdminUser(){
        if (userB == null)
            userB = service.create(new User(
                    null,
                    "jorge",
                    "jorge@gmail.com",
                    "jorge123",
                    Set.of(2)
            ));
        return userB;
    }

    public String token(MockMvc mockMvc) {
        return login(mockMvc, this.getAdminUser());
    }

    public String token(MockMvc mockMvc, User user){
        return login(mockMvc, user);
    }

    public String login(MockMvc mockMvc, User user){
        AuthRequest auth = new AuthRequest();
        auth.setEmail(user.getEmail());
        auth.setPassword(defaultPassword);

        try {

            ObjectWriter writer = new ObjectMapper()
                    .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                    .writer().withDefaultPrettyPrinter();

            String payloadLogin = writer.writeValueAsString(auth);

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadLogin))
                    .andExpect(status().isOk()).andReturn();

            String token = mvcResult.getResponse().getHeader("Authorization");
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
