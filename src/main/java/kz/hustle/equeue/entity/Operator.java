package kz.hustle.equeue.entity;

public class Operator extends User {

    private Integer currentClientNumber;
    private HustleQueue queue;

    public Operator(User user, HustleQueue queue) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getDisplayName(), user.getRole());
        this.queue = queue;
    }

    public void setQueue(HustleQueue queue) {
        this.queue = queue;
    }

    public Integer getCurrent() {
        return currentClientNumber;
    }

    public void callCurrentClient() {
        if (currentClientNumber == null) {
            System.out.println("Client queue is empty");
        } else {
            System.out.println("Repeat: Client with number " + currentClientNumber + " please proceed to operator " + getDisplayName());
        }
    }

    public void callNextClient() {
        Integer next = queue.getNext();
        if (next != null) {
            currentClientNumber = next;
            System.out.println("Client with number " + currentClientNumber + " please proceed to operator " + getDisplayName());
        } else {
            System.out.println("No new clients in the queue");
        }
    }
}
