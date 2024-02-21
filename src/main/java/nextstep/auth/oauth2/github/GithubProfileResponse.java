package nextstep.auth.oauth2.github;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.auth.oauth2.OAuth2UserRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GithubProfileResponse extends OAuth2UserRequest {
    private String email;
    private int age;

    @Override
    public String getUsername() {
        return email;
    }

}
