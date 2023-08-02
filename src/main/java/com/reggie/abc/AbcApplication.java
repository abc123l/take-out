package com.reggie.abc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbcApplication.class, args);
    }

}
