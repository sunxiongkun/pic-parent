package com.sxk.vo;

import lombok.Data;

@Data
public class RetMsg<T> {

  private Integer code;
  private String msg;
  private T data;

}
