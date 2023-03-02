package nextstep.member.application.dto;

import java.util.List;

public class LoginMemberRequest {

    private Long memberId;
    private List<String> roles;

    public LoginMemberRequest() {
    }

    public LoginMemberRequest(final Long memberId, final List<String> roles) {
        this.memberId = memberId;
        this.roles = roles;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<String> getRoles() {
        return roles;
    }
}
