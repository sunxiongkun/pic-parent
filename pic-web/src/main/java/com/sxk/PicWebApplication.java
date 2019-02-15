package com.sxk;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class PicWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(PicWebApplication.class, args);
  }
}
