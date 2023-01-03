package nextstep.member.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubProfileResponse {

    @JsonProperty("email")
    private String email;

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public GithubProfileResponse() {
    }

    public String getEmail() {
        return email;
    }
}
