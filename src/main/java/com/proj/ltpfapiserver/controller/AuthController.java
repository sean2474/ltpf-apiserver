package com.proj.ltpfapiserver.controller;

import com.proj.ltpfapiserver.model.User;
import com.proj.ltpfapiserver.email.service.EmailVerificationService;
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

  @Autowired
  private EmailVerificationService emailVerificationService;

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
      Map<String, Object> response = new HashMap<>();
      String jwtToken = JwtUtil.generateToken(newUser);

      emailVerificationService.sendVerificationEmail(newUser.getEmail());

      newUser.setPassword(hashedPassword);
      newUser.setAdmin(false);
      userMapper.insertUser(newUser);
      response.put("message", "User created successfully");
      response.put("user", newUser);
      response.put("jwt", jwtToken);


      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.internalServerError().body(Collections.singletonMap("message", "Error creating user"));
    }
  }

  @PostMapping("/verify-email")
  public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody HashMap<String, String> reqBody) {
    String emailToVerify = reqBody.get("email");
    int code = 0;
    try {
      code = Integer.parseInt(reqBody.get("code"));
    } catch (NumberFormatException e) {
      return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Verification code should be integer"));
    }

    try {
      if (emailVerificationService.verifyEmail(emailToVerify, code)) {
        User user = userMapper.findByEmail(emailToVerify);
        user.setVerified(true);
        userMapper.verifyUser(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Email verified successfully"));
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid verification code"));
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error verifying email"));
    }
  }
}
