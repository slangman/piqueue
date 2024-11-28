package kz.hustle.equeue.entity;


import javax.persistence.*;

@Entity
@Table(name = "operator")
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
}