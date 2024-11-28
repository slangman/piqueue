package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.repository.OperatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        operator = new Operator((new User("username", "password", "displayname", "user")));
    }

    @Test
    void saveOperator_ShouldAddOperatorToRepository() {
        when(repository.save(operator)).thenReturn(operator);
        Operator savedOperator = operatorService.saveOperator(operator);
        assertNotNull(savedOperator);
        assertNotNull(savedOperator.getUser());
        User savedUser = savedOperator.getUser();
        assertEquals("username", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
        assertEquals("displayname", savedUser.getDisplayName());
        assertEquals("user", savedUser.getRole());
        verify(repository, times(1)).save(operator);
    }

}
