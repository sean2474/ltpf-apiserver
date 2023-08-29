package com.proj.ltpfapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contact {
  private int id;
  private String dateTime;
  private int submitterId;
  private String title;
  private String firstName;
  private String lastName;
  @ToString.Exclude
  private String content;
  @ToString.Exclude
  private String email;
  @ToString.Exclude
  private String phone;
}
