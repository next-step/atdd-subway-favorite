package subway.auth.token.oauth2.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import subway.auth.token.oauth2.OAuth2UserRequest;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubProfileResponse implements OAuth2UserRequest {

    private String email;
    private Integer age;

    @Override
    public String getUsername() {
        return email;
    }
}
