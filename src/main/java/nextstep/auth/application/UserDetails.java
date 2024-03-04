package nextstep.auth.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    Long id = null;
    String email = null;
    String password = null;
    Integer age = null;

    public UserDetails(String email) {
        this.email = email;
    }
}
