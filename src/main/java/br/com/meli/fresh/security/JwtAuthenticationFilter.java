package br.com.meli.fresh.security;

import br.com.meli.fresh.dto.request.AuthRequest;
import br.com.meli.fresh.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        try {
            AuthRequest credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthRequest.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(),
                    credentials.getPassword(),
                    new ArrayList<>()
            );

            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;

        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        String username = ((UserSpringSecurity) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        response.setHeader("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Location", ((UserSpringSecurity) auth.getPrincipal()).getId());
        response.addHeader("Authorization", "Bearer " + token);
    }

}
