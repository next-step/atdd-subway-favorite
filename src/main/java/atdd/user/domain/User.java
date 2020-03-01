package atdd.user.domain;

import atdd.exception.InvalidUserException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

@Getter
public class User {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;

    public User() {
    }

    @Builder
    private User(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void encryptPassword() {
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
