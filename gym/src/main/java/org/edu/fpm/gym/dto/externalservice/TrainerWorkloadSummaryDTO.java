package org.edu.fpm.gym.dto.externalservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrainerWorkloadSummaryDTO (String username, String firstName,
                                         String lastName, boolean isActive,
                                         List<YearSummaryDTO> years) { }

