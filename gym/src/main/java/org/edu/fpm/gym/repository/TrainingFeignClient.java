package org.edu.fpm.gym.repository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.edu.fpm.gym.config.FeignConfig;
import org.edu.fpm.gym.dto.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "trainer-workload-service",
        path = "v1/workload/service/trainer",
        configuration = FeignConfig.class)
public interface TrainingFeignClient {
    @PostMapping("/update")
    ResponseEntity<String> updateWorkload(@RequestBody ExternalTrainingServiceDTO request);

    @GetMapping("/monthly-summary")
    TrainerWorkloadSummaryDTO getMonthlyWorkload(@RequestParam("username") String username);

    @PostMapping("/init")
    ResponseEntity<String> initializeTrainerWorkload(@RequestBody List<ExternalTrainingServiceDTO> trainingData);
}
