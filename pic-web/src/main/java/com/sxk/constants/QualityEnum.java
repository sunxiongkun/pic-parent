package com.sxk.constants;

import lombok.Getter;

@Getter
public enum QualityEnum {

  LOW(1, "1080P壁纸", "1080P壁纸"),
  MIDDLE(2, "4k壁纸", "4k壁纸"),
  HIGH(3, "8k壁纸", "8k壁纸");

  private Integer code;
  private String name;
  private String description;

  QualityEnum(Integer code, String name, String description) {
    this.code = code;
    this.name = name;
    this.description = description;
  }

  public static QualityEnum of(Integer code) {
    QualityEnum[] values = QualityEnum.values();
    for (QualityEnum value : values) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    throw new NullPointerException();
  }
}
