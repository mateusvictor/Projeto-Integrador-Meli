package br.com.meli.fresh.services.impl;

import br.com.meli.fresh.model.Buyer;
import br.com.meli.fresh.model.Seller;
import br.com.meli.fresh.model.User;
import br.com.meli.fresh.model.WarehouseManager;
import br.com.meli.fresh.repository.IBuyerRepository;
import br.com.meli.fresh.repository.ISellerRepository;
import br.com.meli.fresh.repository.IWarehouseManagerRepository;
import br.com.meli.fresh.security.UserSpringSecurity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IWarehouseManagerRepository warehouseManagerRepository;
    private final ISellerRepository sellerRepository;

    @Autowired
    private final IBuyerRepository buyerRepository;

    public UserDetails loadUserByUsername(IWarehouseManagerRepository repository, String email) throws UsernameNotFoundException {
        WarehouseManager user = repository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }

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

    public UserDetails loadUserByUsername(ISellerRepository repository, String email) throws UsernameNotFoundException {
        Seller user = repository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }

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

    public UserDetails loadUserByUsername(IBuyerRepository repository, String email) throws UsernameNotFoundException {
        Buyer user = repository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Buyer user = buyerRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }

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
