package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void createTrainingType_Test() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");

        when(trainingTypeRepository.save(trainingType)).thenReturn(trainingType);
        TrainingType result = trainingTypeService.createTrainingType(trainingType);

        verify(trainingTypeRepository).save(trainingType);
        assertEquals(trainingType, result);
    }

    @Test
    void findTrainingTypeByName_Test() {
        String trainingTypeName = "Yoga";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeName);

        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingTypeName)).thenReturn(trainingType);
        TrainingType result = trainingTypeService.findTrainingTypeByName(trainingTypeName);

        verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(trainingTypeName);
        assertEquals(trainingType, result);
    }
}
