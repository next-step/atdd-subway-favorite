package nextstep.auth.oauth2.github.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.auth.oauth2.dto.OAuth2UserRequest;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubProfileResponse implements OAuth2UserRequest {

    @Getter
    private String email;

    private Integer age;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}
