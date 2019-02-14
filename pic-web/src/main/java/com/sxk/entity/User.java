package com.sxk.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @javax.persistence.Id
  @GeneratedValue
  private Integer id;
  private String description;
  private BigDecimal price;
  private String imageUrl;
}
