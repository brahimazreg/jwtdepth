package com.brahim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl  implements UserDetailsService {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    MyUserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        // todo fetch data from database
        try {
            com.brahim.model.User userdb =userService.getUser(email);
            if(userdb != null) {
                Collection<GrantedAuthority> autorities = new ArrayList<>();
                userdb.getRoles().forEach(r -> autorities.add(new SimpleGrantedAuthority(r.getName())));
                //userdb.getRoles().forEach(r -> autorities.add(new SimpleGrantedAuthority("ROLE_" + r.getName())));
                return new User(userdb.getEmail(), userdb.getPassword(),autorities);
            }else {
                throw  new UsernameNotFoundException("user not found ");
            }
        }catch (Exception e) {
            throw  new UsernameNotFoundException("user not found ");
        }
    }
}
