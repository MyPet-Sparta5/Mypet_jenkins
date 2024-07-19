package com.sparta.mypet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MypetApplication {

    public static void main(String[] args) {
        SpringApplication.run(MypetApplication.class, args);
    }

}
