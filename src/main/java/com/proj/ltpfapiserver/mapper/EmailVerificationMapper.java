package com.proj.ltpfapiserver.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.proj.ltpfapiserver.model.EmailVerification;

@Mapper
public interface EmailVerificationMapper {
  @Insert("INSERT INTO User.verification_codes (email, code, expireDate) VALUES (#{email}, #{code}, #{expireDate})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertVerificationCode(EmailVerification emailVerification);

  @Select("SELECT * FROM User.verification_codes WHERE email = #{email}")
  EmailVerification findByEmail(String email);
}
