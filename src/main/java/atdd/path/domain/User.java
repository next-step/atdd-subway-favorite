package atdd.path.domain;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "is_deleted='false'")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public User() {
    }

    private User(String email,
                 String name,
                 String password,
                 boolean isDeleted) {

        this.email = email;
        this.name = name;
        this.password = password;
        this.isDeleted = isDeleted;
    }

    public static User of(String email,
                          String name,
                          String password) {

        return new User(email, name, password, false);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}

