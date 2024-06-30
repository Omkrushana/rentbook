package com.crio.rentread.service;

import com.crio.rentread.model.User;
import com.crio.rentread.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email:" + email));

        Set<GrantedAuthority> grantedAuthorities = Collections
                .singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), grantedAuthorities);
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByEmail(email)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found with email:
    // " + email));

    // List<GrantedAuthority> authorities = new ArrayList<>();
    // authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    // System.out.println("Load user method called");

    // return new
    // org.springframework.security.core.userdetails.User(user.getEmail(),
    // user.getPassword(), authorities);
    // }

}
