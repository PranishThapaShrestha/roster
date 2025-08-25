package com.nff.roster.security;

import com.nff.roster.entity.User;
import com.nff.roster.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository users;


    public UserDetailsServiceImpl(UserRepository users) {

        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));
        return new UserDetailsImpl(user);

    }
}
