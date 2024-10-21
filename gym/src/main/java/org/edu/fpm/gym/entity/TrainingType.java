package org.edu.fpm.gym.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "training_type")
public class TrainingType {
//    YOGA,
//    PILATES,
//    AEROBICS,
//    WORKOUT,
//    CARDIO;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_training_type", nullable = false)
    private int id;

    @Column(name = "training_type_name", nullable = false, length = 50)
    private String trainingTypeName;

}
