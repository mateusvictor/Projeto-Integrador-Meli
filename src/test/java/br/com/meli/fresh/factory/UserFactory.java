package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.User;
import br.com.meli.fresh.security.UserSpringSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserFactory {


    public static User getUserEntityA(){
        User user = new User(
                "user1",
                "mateus",
                "mateus@gmail.com",
                "1234",
                Set.of(0, 1, 2, 3));

        return user;
    }

    public static User getUserEntityB(){
        User user = new User(
                "user2",
                "joana",
                "joana@gmail.com",
                "1234",
                Set.of(0, 1, 2, 3));

        return user;
    }

    public static User getUserEntityC(String email){
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        User user = User.builder().name("testecomment").email(email).password(pe.encode("123")).roles(Set.of(0, 1, 2, 3)).build();

        return user;
    }

    public static User getUserNonAuthorized(){
        User user = new User(
                "user3",
                "mario",
                "mario@gmail.com",
                "1234",
                Set.of(0, 1));
        return user;
    }

    public static UserSpringSecurity getUserSS(User user){
        Collection<GrantedAuthority> grantedAuthorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        UserSpringSecurity userSS = new UserSpringSecurity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
        return userSS;
    }
}
