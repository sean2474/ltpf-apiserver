package com.proj.ltpfapiserver.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proj.ltpfapiserver.mapper.UserMapper;
import com.proj.ltpfapiserver.model.User;
import com.proj.ltpfapiserver.util.JwtUtil;
import com.proj.ltpfapiserver.util.SecurityUtil;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000, http://ltpf-test.com:3000, http://ltpf.org")
public class UserController {

  @Autowired
  private UserMapper userMapper;

  @GetMapping()
  public ResponseEntity<Map<String, Object>> getUser(@RequestHeader HashMap<String, String> JWT) {
    String jwt = JWT.get("authorization").split(" ")[1];
    if (jwt != null) {
      String username;
      try {
        username = JwtUtil.decode(jwt).getSubject();
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid JWT"));
      }
      User foundUser = userMapper.findByUsername(username);
      if (foundUser == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
      }
      Map<String, Object> response = new HashMap<>();
      response.put("message", "User found successfully");
      foundUser.setPassword(null);
      response.put("user", foundUser);
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "No JWT provided"));
  }

  @PutMapping() 
  public ResponseEntity<Map<String, Object>> updateUser(@RequestBody User user, @RequestHeader HashMap<String, String> JWT) {
    String jwt = JWT.get("authorization").split(" ")[1];
    if (jwt != null) {
      String username;
      try {
        username = JwtUtil.decode(jwt).getSubject();
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid JWT"));
      }
      User foundUser = userMapper.findByUsername(username);
      if (foundUser == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
      }
      try {
        if (SecurityUtil.verifyPassword(user.getPassword(), foundUser.getPassword())) {
          userMapper.changeUser(user);
          Map<String, Object> response = new HashMap<>();
          response.put("message", "User updated successfully");
          user.setPassword(null);
          response.put("user", user);
          return ResponseEntity.ok(response);
        } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
        }
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Error verifying password"));
      }
    }
    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "No JWT provided"));
  }

  @DeleteMapping() 
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

  @PostMapping("/check-username-exists")
  public ResponseEntity<Map<String, Object>> checkUsernameExists(@RequestBody HashMap<String, String> user) {
    String username = user.get("username");
    User foundUser = userMapper.findByUsername(username);
    if (foundUser == null) {
      return ResponseEntity.ok(Collections.singletonMap("message", "Username does not exist"));
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Username already exists"));
  }

  @PostMapping("/check-email-exists") 
  public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestBody HashMap<String, String> user) {
    String email = user.get("email");
    User foundUser = userMapper.findByEmail(email);
    if (foundUser == null) {
      return ResponseEntity.ok(Collections.singletonMap("message", "Email does not exist"));
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Email already exists"));
  }

  @PostMapping("/check-phone-exists")
  public ResponseEntity<Map<String, Object>> checkPhoneExists(@RequestBody HashMap<String, String> user) {
    String phone = user.get("phone");
    User foundUser = userMapper.findByPhone(phone);
    if (foundUser == null) {
      return ResponseEntity.ok(Collections.singletonMap("message", "Phone does not exist"));
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Phone already exists"));
  }
}
