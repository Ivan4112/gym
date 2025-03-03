package org.edu.fpm.gym.dto.externalservice;

import java.util.List;

public record YearSummaryDTO(int year, List<MonthSummaryDTO> months) {}
