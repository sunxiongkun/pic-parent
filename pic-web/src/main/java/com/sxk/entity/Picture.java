package com.sxk.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "picture")
public class Picture {

  @javax.persistence.Id
  @GeneratedValue
  private Integer id;
  private Integer category; //
  private Integer quality; //
  private String name;
  private String picUrl;
  private String netUrl;
  private String vipUrl;
  private String keyWord;
  private String description;
  private String resolution; //分辨率
  private Integer status = 0;
  @Temporal(TemporalType.TIMESTAMP)
  private Date createTime = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime = new Date();
}
