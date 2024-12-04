package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.repository.OperatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperatorServiceTest {
    private OperatorService operatorService;
    private OperatorRepository repository;

    @MockBean
    private HustleQueueService queueService;

    private Operator operator;

    @BeforeEach
    void setup() {
        repository = mock(OperatorRepository.class);
        operatorService = new OperatorService(repository, queueService);
        MockitoAnnotations.openMocks(this);
        operator = new Operator((new User(100L, "username", "password", "displayname", "user")));
        operator.setId(42L);
    }

    @Test
    void saveOperator_ShouldReturnSavedOperator() {
        // Arrange
        when(repository.save(operator)).thenReturn(operator);

        // Act
        Operator savedOperator = operatorService.saveOperator(operator);

        // Assert
        assertNotNull(savedOperator);
        assertEquals(42L, savedOperator.getId());
        assertNotNull(savedOperator.getUser());
        User savedUser = savedOperator.getUser();
        assertEquals("username", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertEquals("displayname", savedUser.getDisplayName());
        assertEquals("user", savedUser.getRole());

        // Verify interaction
        verify(repository, times(1)).save(operator);
    }

    @Test
    void getOperator_ShouldReturnOperator_WhenExists() {
        // Arrange
        when(repository.findById(42L)).thenReturn(Optional.of(operator));

        // Act
        Operator foundOperator = operatorService.getOperator(42L);

        // Assert
        assertNotNull(foundOperator);
        assertEquals(42L, foundOperator.getId());
        assertNotNull(foundOperator.getUser());
        User foundUser = foundOperator.getUser();
        assertEquals("username", foundUser.getUsername());
        assertEquals("password", foundUser.getPassword());
        assertEquals("displayname", foundUser.getDisplayName());
        assertEquals("user", foundUser.getRole());

        // Verify interaction
        verify(repository, times(1)).findById(42L);
    }

    @Test
    void getOperator_ShouldReturnNull_WhenNotExists() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Operator foundOperator = operatorService.getOperator(1L);

        // Assert
        assertNull(foundOperator);

        // Verify interaction
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getOperatorByUserId_ShouldReturnOperator_WhenExists() {
        // Arrange
        when(repository.findByUserId(100L)).thenReturn(Optional.of(operator));

        // Act
        Optional<Operator> result = operatorService.getOperatorByUserId(100L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(42L, result.get().getId());
        assertEquals(100L, result.get().getUser().getId());

        // Verify interaction
        verify(repository, times(1)).findByUserId(100L);
    }

    @Test
    void getOperatorByUserId_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(repository.findByUserId(100L)).thenReturn(Optional.empty());

        // Act
        Optional<Operator> result = operatorService.getOperatorByUserId(100L);

        // Assert
        assertFalse(result.isPresent());

        // Verify interaction
        verify(repository, times(1)).findByUserId(100L);
    }
}
