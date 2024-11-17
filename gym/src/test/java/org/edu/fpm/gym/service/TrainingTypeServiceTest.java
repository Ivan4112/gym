package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");
    }


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

    @Test
    void testFindAllTrainingTypes() {
        List<TrainingType> trainingTypes = List.of(trainingType);
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeService.findAllTrainingTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getTrainingTypeName());
        verify(trainingTypeRepository, times(1)).findAll();
    }
}
