package org.edu.fpm.gym;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.edu.fpm")
public class GymApplication {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(GymApplication.class);
    }

}
