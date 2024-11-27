package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    private final Terminal terminal;
    private final HustleQueueService queueService;

    @Autowired
    public TerminalService(Terminal terminal, HustleQueueService queueService) {
        this.terminal = terminal;
        this.queueService = queueService;
    }

    public void addToQueue() {
        int clientNumber =  queueService.addToQueue();
        System.out.println("Client with number " + clientNumber + " added to the queue.");
    }
}
