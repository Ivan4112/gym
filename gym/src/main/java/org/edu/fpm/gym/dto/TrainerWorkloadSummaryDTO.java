package org.edu.fpm.gym.dto;

import java.util.Map;

public record TrainerWorkloadSummaryDTO (String username, String firstName,
                                         String lastName, boolean isActive,
                                         Map<Integer, Map<Integer, Integer>> monthlySummary) { }
