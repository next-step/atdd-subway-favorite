package nextstep.subway.auth.dto;

import nextstep.subway.auth.domain.LoginMember;

public class LoginMemberPrincipal {

    private Long id;

    private String email;

    public LoginMemberPrincipal() {}

    public LoginMemberPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static LoginMemberPrincipal of(LoginMember loginMember) {
        return new LoginMemberPrincipal(loginMember.getId(), loginMember.getEmail());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
