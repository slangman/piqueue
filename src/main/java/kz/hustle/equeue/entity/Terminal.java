package kz.hustle.equeue.entity;

public class Terminal {

    private String name;
    private HustleQueue queue;

    public Terminal(String name, HustleQueue queue) {
        this.name = name;
        this.queue = queue;
    }

    public void addToQueue() {
        int clientNumber =  queue.addToQueue();
        System.out.println("Client with number " + clientNumber + " added to the queue.");
    }
}
