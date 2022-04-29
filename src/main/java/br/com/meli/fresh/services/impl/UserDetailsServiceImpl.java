package br.com.meli.fresh.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Buyer user = buyerRepository.findByEmail(email);
//        if(user == null){
//            throw new UsernameNotFoundException(email);
//        }
//
//        Collection<GrantedAuthority> grantedAuthorities = user
//                .getRoles()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//
//        UserSpringSecurity userSS = new UserSpringSecurity(
//                user.getId(),
//                user.getEmail(),
//                user.getPassword(),
//                grantedAuthorities
//        );
//        return userSS;
//    }
}
