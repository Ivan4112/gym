package org.edu.fpm.gym;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.service.TraineeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class GymApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GymApplication.class, args);

		/*TraineeService traineeService = context.getBean(TraineeService.class);
		Trainee trainee = traineeService.createTrainee("Ivan", "Bondar",
				LocalDate.of(2000, 1, 1), "123 Main St");
		Trainee trainee2 = traineeService.createTrainee("Ivan", "Bondar",
				LocalDate.of(2000, 1, 1), "123 Main St");
		System.out.println("Generated login: " + trainee.getUsername());
		System.out.println("Generated password: " + trainee.getPassword());

		System.out.println("Generated login: " + trainee2.getUsername());
		System.out.println("Generated password: " + trainee2.getPassword());*/
	}

}
