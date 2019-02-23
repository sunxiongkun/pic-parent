package com.sxk.constants;

import lombok.Getter;

@Getter
public enum CategoryEnum {

  SCENIC(1, "4k风景", "4k风景高清图片"),
  BACKGROUND(2, "4k背景", "4k背景高清图片"),
  BELLE(3, "4k美女", "4k美女高清图片"),
  GAME(4, "4k游戏", "4k游戏高清图片"),
  ANIME(5, "4k动漫", "4k动漫高清图片"),
//  FILM(6, "4k影视", "4k影视高清图片"),
//  STAR(7, "4k明星", "4k明星高清图片"),
//  ANIMAL(8, "4k动物", "4k动物高清图片"),
//  FIGURE(9, "4k人物", "4k人物高清图片"),
  FOOD(10, "4k美食", "4k美食高清图片"),
  CAR(11, "4k汽车", "4K汽车高清图片");

  private Integer code;
  private String name;
  private String description;

  CategoryEnum(Integer code, String name, String description) {
    this.code = code;
    this.name = name;
    this.description = description;
  }

  public static CategoryEnum of(Integer code) {
    CategoryEnum[] values = CategoryEnum.values();
    for (CategoryEnum value : values) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    throw new NullPointerException();
  }
}
