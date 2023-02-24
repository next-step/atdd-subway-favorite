package nextstep.login.github;

import static nextstep.login.github.GithubResponses.*;

import org.springframework.stereotype.Service;

@Service
public class GithubAuthService {

	public GithubResponse findUserByCode(GithubRequest githubRequest) {
		GithubResponses githubResponses = findByCode(githubRequest.getCode());
		return new GithubResponse(githubResponses.getAccessToken());
	}
}
