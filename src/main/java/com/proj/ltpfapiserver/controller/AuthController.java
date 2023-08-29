package com.proj.ltpfapiserver.controller;

import com.proj.ltpfapiserver.model.User;
import com.proj.ltpfapiserver.mapper.UserMapper;
import com.proj.ltpfapiserver.util.JwtUtil;
import com.proj.ltpfapiserver.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000, http://ltpf-test.com:3000, http://ltpf.org")
public class AuthController {

  @Autowired
  private UserMapper userMapper;

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> verifyUser(@RequestBody HashMap<String, String> user) {
    String username = user.get("username");
    String password = user.get("password");

    User foundUser = userMapper.findByUsername(username);
    try {
      if (foundUser != null && SecurityUtil.verifyPassword(password, foundUser.getPassword())) {
        String jwtToken = JwtUtil.generateToken(foundUser);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User verified successfully");
        response.put("jwt", jwtToken);
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error verifying password"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, Object>> createUser(@RequestBody User newUser) {
    try {
      if (userMapper.findByUsername(newUser.getUsername()) != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username already exists"));
      }
      if (userMapper.findByEmail(newUser.getEmail()) != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Email already exists"));
      }
      if (Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$").matcher(newUser.getPassword()).matches() == false) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character"));
      }
      String hashedPassword = SecurityUtil.hashPassword(newUser.getPassword());
      newUser.setPassword(hashedPassword);
      newUser.setAdmin(false);
      userMapper.insertUser(newUser);
      Map<String, Object> response = new HashMap<>();
      response.put("message", "User created successfully");
      response.put("user", newUser);
      String jwtToken = JwtUtil.generateToken(newUser);
      response.put("jwt", jwtToken);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.internalServerError().body(Collections.singletonMap("message", "Error creating user"));
    }
  }

  @GetMapping("/check-email")
  public boolean isEmailUnique(@RequestParam String email) {
    User user = userMapper.findByEmail(email);
    return user == null; 
  }

  @GetMapping("/check-username")
  public boolean isUsernameUnique(@RequestParam String username) {
    User user = userMapper.findByUsername(username);
    return user == null; 
  }

  @GetMapping("/check-phone")
  public boolean isPhoneUnique(@RequestParam String phone) {
    User user = userMapper.findByPhone(phone);
    return user == null;
  }
}
