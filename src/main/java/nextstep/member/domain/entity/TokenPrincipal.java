package nextstep.member.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPrincipal {
    private Long subject;
    private String email;

    public static TokenPrincipal of(Member member) {
        return new TokenPrincipal(member.getId(), member.getEmail());
    }
}
