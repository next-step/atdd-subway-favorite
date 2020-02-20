package atdd.path.domain.entity;

import atdd.path.application.exception.InvalidUserException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table
public class User extends BaseEntity {
    @Column
    private String name;
    @Column
    private String email;

    @Setter
    @Column
    @JsonIgnore
    private String password;

    public User() {
    }

    @Builder
    private User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void passwordEncrypt() {
        this.password = encrypt(this.password);
    }

    public void checkPassword(String password) {
        if (BCrypt.checkpw(password, encrypt(this.password)))
            throw new InvalidUserException();
    }

    private String encrypt(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}