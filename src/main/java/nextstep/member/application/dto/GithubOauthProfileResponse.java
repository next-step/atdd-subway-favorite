package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubOauthProfileResponse {

    private String email;

    private Long id;
}
