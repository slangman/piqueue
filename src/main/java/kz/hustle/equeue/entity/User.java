package kz.hustle.equeue.entity;

import javax.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Operator operator;

    private String password;
    private String displayName;
    private String role;

    public User() {
    }

    public User(String username, String password, String displayName, String role) {
        //this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
    }

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.displayName = userDto.getDisplayName();
        this.role = userDto.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void update(User newUser) {
        if (newUser.getPassword() != null) {
            setPassword(newUser.getPassword());
        }
        if (newUser.getUsername() != null) {
            setUsername(newUser.getUsername());
        }
        if (newUser.getDisplayName() != null) {
            setDisplayName(newUser.getDisplayName());
        }
    }
}
