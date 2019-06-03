package ru.puga1chev.springrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@SpringBootApplication(
        exclude = {
                SecurityAutoConfiguration.class
        },
        scanBasePackages = "ru.puga1chev.springrest"
)
public class SpringrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringrestApplication.class, args);
    }

}
