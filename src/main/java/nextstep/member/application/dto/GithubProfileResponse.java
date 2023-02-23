package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubProfileResponse {

    private String email;

    public static GithubProfileResponse from(String email) {
        return new GithubProfileResponse(email);
    }
}
