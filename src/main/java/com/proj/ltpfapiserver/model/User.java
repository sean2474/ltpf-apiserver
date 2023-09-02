package com.proj.ltpfapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class User {
  private int id;
  private boolean isAdmin;
  @NonNull
  private String firstName;
  @NonNull
  private String lastName;
  @NonNull
  @ToString.Exclude
  private String email;
  private String phone;
  private String username;
  @ToString.Exclude
  private String password;
  @ToString.Exclude
  private String address;
  private String accountType; // email, google, naver, kakao
  private boolean isVerified;
}