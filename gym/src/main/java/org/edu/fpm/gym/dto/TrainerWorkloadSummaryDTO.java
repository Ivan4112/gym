package org.edu.fpm.gym.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrainerWorkloadSummaryDTO (String username, String firstName,
                                         String lastName, boolean isActive,
                                         Map<Integer, Map<Integer, Integer>> monthlySummary) { }
