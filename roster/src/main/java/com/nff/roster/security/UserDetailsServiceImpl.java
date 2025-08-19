package com.nff.roster.security;

import com.nff.roster.entity.User;
import com.nff.roster.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository users;
    private final UserDetailsImpl userDetails;

    public UserDetailsServiceImpl(UserRepository users, UserDetailsImpl userDetails){
        this.userDetails=userDetails;
        this.users=users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= users.findByUsername(username);



        return null;
    }
}
