package com.proj.ltpfapiserver.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proj.ltpfapiserver.mapper.ItemMapper;
import com.proj.ltpfapiserver.mapper.UserMapper;
import com.proj.ltpfapiserver.model.Item;
import com.proj.ltpfapiserver.model.User;
import com.proj.ltpfapiserver.util.JwtUtil;

@RestController
@RequestMapping("/data/item")
public class ItemController {
  @Autowired
  private ItemMapper itemMapper;

  @Autowired
  private UserMapper userMapper;

  @PostMapping()
  public ResponseEntity<Map<String, Object>> createItem(@RequestBody Item item, @RequestHeader("Authorization") String authHeader) {
    if (!isAdmin(authHeader)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Access denied"));
    }
    itemMapper.insertItem(item);
    return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Item created successfully"));
  }

  @GetMapping()
  public ResponseEntity<List<Item>> getAllItems() {
    List<Item> items = itemMapper.findAll();
    return ResponseEntity.ok(items);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable int id) {
    Item item = itemMapper.findById(id);
    if (item == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity.ok(item);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateItem(@PathVariable int id, @RequestHeader("Authorization") String authHeader, @RequestBody Item item) {
    if (!isAdmin(authHeader)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Access denied"));
    }
    Item existingItem = itemMapper.findById(id);
    if (existingItem == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Item not found"));
    }
    item.setId(id); 
    itemMapper.updateItem(item);
    return ResponseEntity.ok(Collections.singletonMap("message", "Item updated successfully"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable int id, @RequestHeader("Authorization") String authHeader) {
    if (!isAdmin(authHeader)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "Access denied"));
    }
    Item existingItem = itemMapper.findById(id);
    if (existingItem == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Item not found"));
    }
    itemMapper.deleteItem(id);
    return ResponseEntity.ok(Collections.singletonMap("message", "Item deleted successfully"));
  }
    
  private boolean isAdmin(String authHeader) {
    String jwt = authHeader.split(" ")[1];
    String username = JwtUtil.decode(jwt).getSubject();
    User user = userMapper.findByUsername(username);
    return user != null && user.isAdmin();
  }
}