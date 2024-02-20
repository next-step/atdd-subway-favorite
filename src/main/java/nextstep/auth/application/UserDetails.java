package nextstep.auth.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetails {
    private Long id;
    private String password;
    private String email;
}
