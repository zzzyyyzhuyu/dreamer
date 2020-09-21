package com.wimp.dreamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author zy
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class DreamerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamerApplication.class, args);
    }

}
