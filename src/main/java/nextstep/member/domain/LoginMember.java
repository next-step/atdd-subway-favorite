package nextstep.member.domain;

import lombok.Getter;

@Getter
public class LoginMember {
    private Long id;
    private String email;

    public LoginMember(String email) {
        this.email = email;
    }
}
