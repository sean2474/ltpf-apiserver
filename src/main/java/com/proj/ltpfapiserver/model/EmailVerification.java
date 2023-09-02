package com.proj.ltpfapiserver.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification {
  private int id;
  private String email;
  private int code;
  private LocalDateTime expireDate;
}
