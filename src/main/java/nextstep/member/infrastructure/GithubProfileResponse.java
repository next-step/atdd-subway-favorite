package nextstep.member.infrastructure;

import nextstep.member.domain.Member;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Member toMember() {
        return new Member(this.email);
    }
}
