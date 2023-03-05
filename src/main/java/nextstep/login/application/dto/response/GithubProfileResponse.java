package nextstep.login.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(final String email) {
        this.email = email;
    }
}
