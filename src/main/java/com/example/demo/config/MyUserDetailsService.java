package com.example.demo.config;

import com.example.demo.entity.MyUser;
import com.example.demo.repository.MyUserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByUserLogin(username).orElseThrow(() -> new UsernameNotFoundException(StringUtils.EMPTY));
        return new MyAppUser(myUser);

    }

    public static class MyAppUser extends User {

        @Getter @Setter private MyUser myUser;

        public MyAppUser (MyUser myUser) {
            super(myUser.getUserLogin(), myUser.getUserPassword(), Collections.singletonList(new SimpleGrantedAuthority(myUser.getUserRole())));
        }

    }
}


