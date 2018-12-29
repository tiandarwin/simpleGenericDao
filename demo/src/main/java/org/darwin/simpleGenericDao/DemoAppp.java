package org.darwin.simpleGenericDao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author hexiufeng
 * @date 2018/12/29上午11:10
 */
@SpringBootApplication
@ImportResource("classpath:app-context.xml")
public class DemoAppp {


  public static void main(String[] args) {
    SpringApplication.run(DemoAppp.class, args);
  }
}
