package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.repository.HustleQueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class HustleQueueServiceUnitTest {
    private HustleQueueRepository repository;
    private HustleQueueService hustleQueueService;

    @BeforeEach
    void setup() {
        repository = mock(HustleQueueRepository.class);
        hustleQueueService = new HustleQueueService(repository);
    }

    @Test
    void addToQueue_ShouldAddFirstElement_WhenQueueIsEmpty() {
        //Arrange
        when(repository.findAll()).thenReturn(new ArrayList<>());

        //Act
        int position = hustleQueueService.addToQueue();

        //Assert
        assertEquals(1, position);
        verify(repository, times(1)).save(any(HustleQueue.class));
    }

    @Test
    void addToQueue_ShouldAddElementWithNextPosition_WhenQueueIsNotEmpty() {
        //Arrange
        List<HustleQueue> existingQueue = new ArrayList<>();
        HustleQueue lastEntity = new HustleQueue();
        lastEntity.setPosition(5);
        existingQueue.add(lastEntity);

        when(repository.findAll()).thenReturn(existingQueue);

        //Act
        int position = hustleQueueService.addToQueue();

        //Assert
        assertEquals(6, position);
        verify(repository, times(1)).save(any(HustleQueue.class));
    }

    @Test
    void addToQueue_ShouldCallRepositoryMethodsCorrectly() {
        //Arrange
        when(repository.findAll()).thenReturn(new ArrayList<>());

        //Act
        hustleQueueService.addToQueue();

        // Assert
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(any(HustleQueue.class));
    }


    @Test
    void getNext_ShouldReturnPositionOfTheFirstRecordInQueue_IfQueueIsNotEmpty() {
        //Arrange
        List<HustleQueue> existingQueue = new ArrayList<>();
        existingQueue.add(new HustleQueue(5));
        existingQueue.add(new HustleQueue(6));

        when(repository.findAll()).thenReturn(existingQueue);

        //Act
        int position = hustleQueueService.getNext();

        //Assert
        assertEquals(5, position);
    }

    @Test
    void getNext_ShouldReturnNull_IfQueueIsNotEmpty() {
        //Arrange
        List<HustleQueue> existingQueue = new ArrayList<>();

        when(repository.findAll()).thenReturn(existingQueue);

        Integer position = hustleQueueService.getNext();
        assertNull(position);
    }


    @Test
    void resetQueue_ShouldCallDeleteAll() {
        //Act
        hustleQueueService.resetQueue();

        verify(repository, times(1)).deleteAll();
    }
}
