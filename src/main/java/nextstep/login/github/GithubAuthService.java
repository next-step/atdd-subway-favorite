package nextstep.login.github;

import static nextstep.login.github.GithubResponses.*;

import org.springframework.stereotype.Service;

import nextstep.auth.AuthGithub;

@Service
public class GithubAuthService {

	public GithubAccessTokenResponse findUserByCode(GithubAccessTokenRequest githubAccessTokenRequest) {
		GithubResponses githubResponses = findByCode(githubAccessTokenRequest.getCode());
		return new GithubAccessTokenResponse(githubResponses.getAccessToken());
	}

	public GithubProfileResponse findUserByToken(AuthGithub accessTokenRequest) {
		GithubResponses githubResponses = findByToken(accessTokenRequest.getAccessToken());
		return new GithubProfileResponse(githubResponses.getEmail());
	}
}
