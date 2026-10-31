package com.example.ocms.source.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ocms.source.model.User;
import com.example.ocms.source.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
  @Autowired
  private UserRepository users;
  @Autowired
  private PasswordEncoder encoder;
  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiration}")
  private long expiration;

  public User register(String email, String password, String role) {
    if (users.findByEmail(email).isPresent())
      throw new IllegalArgumentException("Email already registered");
    if (!"INSTRUCTOR".equals(role) && !"STUDENT".equals(role))
      role = "STUDENT";
    User user = new User(email, encoder.encode(password), role, "INSTRUCTOR".equals(role));
    return users.save(user);
  }

  public String login(String email, String password) {
    Optional<User> found = users.findByEmail(email);
    if (!found.isPresent())
      throw new IllegalArgumentException("Invalid credentials");
    User user = found.get();
    if (!encoder.matches(password, user.getPassword()))
      throw new IllegalArgumentException("Invalid credentials");
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiration);
    return Jwts.builder().subject(user.getEmail()).claim("role", user.getRole()).issuedAt(now).expiration(expiry)
        .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
  }

  public String emailFromToken(String token) {
    return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).build()
        .parseSignedClaims(token).getPayload().getSubject();
  }

  public User current(String token) {
    if (token == null || !token.startsWith("Bearer "))
      throw new IllegalArgumentException("Authentication required");
    Optional<User> found = users.findByEmail(emailFromToken(token.substring(7)));
    if (!found.isPresent())
      throw new IllegalArgumentException("User not found");
    return found.get();
  }
}
