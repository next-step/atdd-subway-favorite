package nextstep.member.infra.dto;

import nextstep.member.auth.OAuth2User;

public class GithubProfileResponse implements OAuth2User {

    private String email;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email) {
        this.email = email;
    }


    @Override
    public String getName() {
        return email;
    }
}
