package nextstep.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPrincipal {
    private Long subject;
    private String email;
}
