package auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.OAuth2UserRequest;

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
