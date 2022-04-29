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

    public String token(MockMvc mockMvc) {
        // Creating an admin user with all access.
        User u = service.create(new User(
           null,
           "admin",
           "admin@admin.com",
           "admin",
                Set.of(0, 1, 2, 3)
        ));

        // Setting payload login's request
        AuthRequest auth = new AuthRequest();
        auth.setEmail(u.getEmail());
        auth.setPassword("admin");

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
