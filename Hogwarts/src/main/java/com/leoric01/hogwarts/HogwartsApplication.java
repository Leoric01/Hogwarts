package com.leoric01.hogwarts;

import com.leoric01.hogwarts.models.artifact.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1L,1L);
    }

}
