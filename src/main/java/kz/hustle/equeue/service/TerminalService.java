package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Terminal;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    private final Terminal terminal;
    private final HustleQueueService queueService;
    private static final Logger logger = LoggerFactory.getLogger(TerminalService.class);

    @Autowired
    public TerminalService(Terminal terminal, HustleQueueService queueService) {
        this.terminal = terminal;
        this.queueService = queueService;
    }

    public void addToQueue() {
        int clientNumber = queueService.addToQueue();
        logger.info("Client with number {} added to the queue.", clientNumber);
    }
}
