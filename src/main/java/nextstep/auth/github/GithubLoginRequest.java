package nextstep.auth.github;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubLoginRequest {
	private String code;
	private String clientId;
	private String clientSecret;
}
