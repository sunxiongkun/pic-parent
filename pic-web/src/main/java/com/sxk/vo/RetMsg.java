package com.sxk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetMsg<T> {

  private Integer code;
  private String msg;
  private T data;

}
