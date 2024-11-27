package kz.hustle.equeue.entity;


import javax.persistence.*;

@Entity
@Table(name="operator")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer currentClientNumber;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Operator() {
    }

    public Operator(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCurrent() {
        return currentClientNumber;
    }

    public void setCurrentClientNumber(Integer currentClientNumber) {
        this.currentClientNumber = currentClientNumber;
    }

    /*
    public void callCurrentClient() {
        if (currentClientNumber == null) {
            System.out.println("Client queue is empty");
        } else {
            System.out.println("Repeat: Client with number " + currentClientNumber + " please proceed to operator " + user.getDisplayName());
        }
    }

     */

    /*
    public void callNextClient() {
        Integer next = queue.getNext();
        if (next != null) {
            currentClientNumber = next;
            System.out.println("Client with number " + currentClientNumber + " please proceed to operator " + user.getDisplayName());
        } else {
            System.out.println("No new clients in the queue");
        }
    }
     */
}

/*
public class Operator extends User {

    private Integer currentClientNumber;
    private HustleQueue queue;

    public Operator(User user, HustleQueue queue) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getDisplayName(), user.getRole());
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
*/