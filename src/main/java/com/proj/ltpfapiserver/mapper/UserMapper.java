package com.proj.ltpfapiserver.mapper;

import com.proj.ltpfapiserver.model.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Select("SELECT * FROM User.User WHERE username = #{username}")
  User findByUsername(@Param("username") String username);

  @Insert("INSERT INTO User.User (isAdmin, firstName, lastName, email, phone, username, password, address) VALUES (#{isAdmin}, #{firstName}, #{lastName}, #{email}, #{phone}, #{username}, #{password}, #{address})")
  void insertUser(User user);

  @Insert("UPDATE User.User SET isAdmin = #{isAdmin}, firstName = #{firstName}, lastName = #{lastName}, email = #{email}, phone = #{phone}, username = #{username}, password = #{password}, address = #{address} WHERE id = #{id}")
  void changeUser(User user);

  @Delete("DELETE FROM User.User WHERE username = #{username}")
  void deleteUser(String username);

  @Select("SELECT * FROM User.User WHERE email = #{email}")
  User findByEmail(String email);

  @Select("SELECT * FROM User.User WHERE phone = #{phone}")
  User findByPhone(String phone);
}
