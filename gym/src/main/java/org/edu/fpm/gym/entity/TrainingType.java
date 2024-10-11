package org.edu.fpm.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TrainingType {
    YOGA("Yoga"),
    PILATES("Pilates"),
    AEROBICS("Aerobics"),
    STRENGTH_TRAINING("Strength Training"),
    CARDIO("Cardio");

    private final String trainingTypeName;
}
