package com.nff.roster.controller;

import com.nff.roster.Dto.AuthRequest;
import com.nff.roster.Dto.AuthResponse;
import com.nff.roster.Dto.RegisterRequest;
import com.nff.roster.entity.Role;
import com.nff.roster.entity.User;
import com.nff.roster.repository.RoleRepository;
import com.nff.roster.repository.UserRepository;
import com.nff.roster.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String roleName = Optional.ofNullable(req.getRole()).orElse("EMPLOYEE").toUpperCase();
        Role role = roleRepo.findByName(roleName).orElseGet(() -> roleRepo.save(Role.builder().name(roleName).build()));

        User u = User.builder().username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .username(req.getFullName())
                .roles(Set.of(role))
                .build();

        userRepo.save(u);

        String token = jwtUtil.generateToken(u.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        var opt = userRepo.findByUsername(req.getUsername());
        if (opt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        var user = opt.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            return ResponseEntity.status(401).body("Invalid credentials");

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}