package com.nff.roster.security;

import com.nff.roster.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsImpl userDetailsImpl;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository, UserDetailsImpl userDetailsImpl,
                         UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailsImpl = userDetailsImpl;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = jwtService.parse(token).getBody().getSubject();
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                System.out.println("Invalid ");
            }
        }


    }
}
