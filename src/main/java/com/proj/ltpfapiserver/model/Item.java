package com.proj.ltpfapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
  private int id;
  @ToString.Exclude
  private int submitterId;
  private String title;
  @ToString.Exclude
  private String description;
  private String category;              // 휴대용가방 / 신발 / 의류 / 장비 / 훈련용 / 기타
  @ToString.Exclude
  private ArrayList<String> imageUrls;
  @ToString.Exclude
  private String extraInfo;            // purpose, etc.
}
