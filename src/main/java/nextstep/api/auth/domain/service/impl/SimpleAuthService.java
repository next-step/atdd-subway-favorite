package nextstep.api.auth.domain.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.auth.domain.service.AuthService;
import nextstep.client.github.GithubClient;
import nextstep.client.github.dto.GithubAccessTokenResponse;
import nextstep.client.github.dto.GithubUserProfileResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Service
@RequiredArgsConstructor
public class SimpleAuthService implements AuthService {

	private final GithubClient githubClient;

	/**
	 * AuthService의 메서드 책임의 범위를 정해야 한다.
	 * -> code를 통해 token을 받아오고 유저 정보를 가져오는 것까지가 authService의 책임
	 */
	@Override
	public UserPrincipal authenticateWithGithub(String code) {
		GithubAccessTokenResponse response = githubClient.requestGithubToken(code);
		GithubUserProfileResponse profileResponse = githubClient.requestGithubUserProfile(response.getAccessToken());

		return UserPrincipal.of(profileResponse.getEmail());
	}


}
