package com.spaceobj.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhr
 */
@SpringBootApplication
@EnableScheduling
public class SpaceobjProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpaceobjProjectApplication.class, args);
    }

}
