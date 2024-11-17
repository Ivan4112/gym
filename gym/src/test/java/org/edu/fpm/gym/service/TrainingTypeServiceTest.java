package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.trainingType.TrainingTypeDTO;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
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
    private final String trainingTypeName = "Cardio";

    @BeforeEach
    void setUp() {
        trainingType = TestDataFactory.createTrainingType();
    }


    @Test
    void createTrainingType_Test() {
        when(trainingTypeRepository.save(trainingType)).thenReturn(trainingType);
        TrainingType result = trainingTypeService.createTrainingType(trainingType);

        verify(trainingTypeRepository).save(trainingType);
        assertEquals(trainingType, result);
    }

    @Test
    void findTrainingTypeByName_Test() {

        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingTypeName)).thenReturn(trainingType);
        TrainingType result = trainingTypeService.findTrainingTypeByName(trainingTypeName);

        verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(trainingTypeName);
        assertEquals(trainingType, result);
    }

    @Test
    void findAllTrainingTypes_Test() {
        List<TrainingType> trainingTypes = List.of(trainingType);
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingTypeDTO> response = trainingTypeService.findAllTrainingTypes();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(trainingTypeName, response.getFirst().name());
        verify(trainingTypeRepository, times(1)).findAll();
    }
}
