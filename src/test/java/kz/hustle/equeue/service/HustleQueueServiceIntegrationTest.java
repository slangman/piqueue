package kz.hustle.equeue.service;

import kz.hustle.equeue.SwingApp;
import kz.hustle.equeue.controllers.OperatorController;
import kz.hustle.equeue.controllers.TerminalController;
import kz.hustle.equeue.controllers.TestController;
import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.repository.HustleQueueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class HustleQueueServiceIntegrationTest {

    @Autowired
    private HustleQueueService hustleQueueService;

    @MockBean
    private SwingApp swingApp;

    @Autowired
    private HustleQueueRepository hustleQueueRepository;

    @Test
    void resetQueue_ShouldDeleteAllRecords() {
        //Arrange
        HustleQueue queueRecord = new HustleQueue(1);
        hustleQueueRepository.save(queueRecord);

        List<HustleQueue> beforeReset = hustleQueueRepository.findAll();
        assertEquals(1, beforeReset.size());

        //Act
        hustleQueueService.resetQueue();

        //Assert
        List<HustleQueue> afterReset = hustleQueueRepository.findAll();
        assertTrue(afterReset.isEmpty());

    }

}
