package org.edu.fpm.gym;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.service.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@ComponentScan(basePackages = "org.edu.fpm")
public class GymApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
		Trainee trainee = traineeService.createTrainee("Ivan", "Bondar",
				LocalDate.of(2000, 1, 1), "123 Main St");
		Trainee trainee2 = traineeService.createTrainee("Ivan", "Bondar",
				LocalDate.of(2000, 1, 1), "123 Main St");
		System.out.println("Generated login: " + trainee.getUsername());
		System.out.println("Generated password: " + trainee.getPassword());

		System.out.println("Generated login: " + trainee2.getUsername());
		System.out.println("Generated password: " + trainee2.getPassword());
    }

}
