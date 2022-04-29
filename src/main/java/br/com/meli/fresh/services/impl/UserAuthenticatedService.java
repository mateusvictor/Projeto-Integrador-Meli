package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.security.UserSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthenticatedService {

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception err) {
            return null;
        }
    }
}
