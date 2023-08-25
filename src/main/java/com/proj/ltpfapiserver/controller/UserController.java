package com.proj.ltpfapiserver.controller;

import com.proj.ltpfapiserver.model.User;
import com.proj.ltpfapiserver.mapper.UserMapper;
import com.proj.ltpfapiserver.util.JwtUtil;
import com.proj.ltpfapiserver.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

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
            String hashedPassword = SecurityUtil.hashPassword(newUser.getPassword());
            newUser.setPassword(hashedPassword);
            userMapper.insertUser(newUser);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            response.put("user", newUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
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

    @DeleteMapping("/delete") 
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody HashMap<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        User foundUser = userMapper.findByUsername(username);
        if (foundUser == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
        }

        try {
            if (SecurityUtil.verifyPassword(password, foundUser.getPassword())) {
                userMapper.deleteUser(username);
                return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error verifying password"));
        }
    }
}
