package org.edu.fpm.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "training_type")
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_training_type", nullable = false)
    private Integer id;

    @Column(name = "training_type_name", nullable = false, length = 50)
    private String trainingTypeName;

}
