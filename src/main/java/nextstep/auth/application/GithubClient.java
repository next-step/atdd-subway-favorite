package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAuthRequest;
import nextstep.auth.application.dto.GithubAuthResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {
	private final GithubConfigProperties githubConfigProperties;

	public GithubClient(GithubConfigProperties githubConfigProperties) {
		this.githubConfigProperties = githubConfigProperties;
	}

	public String requestGithubToken(String code) {
		GithubAuthRequest githubAccessTokenRequest = new GithubAuthRequest(githubConfigProperties.getClientId(), githubConfigProperties.getClientSecret(), code);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		String accessToken = new RestTemplate()
				.exchange(githubConfigProperties.getUrlAccessToken(),
						HttpMethod.POST,
						new HttpEntity(githubAccessTokenRequest, headers),
						GithubAuthResponse.class)
				.getBody()
				.getAccessToken();

		return accessToken;
	}

	public GithubProfileResponse requestGithubProfile(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, accessToken);

		return new RestTemplate()
				.exchange(githubConfigProperties.getUrlProfile(),
						HttpMethod.GET,
						new HttpEntity(headers),
						GithubProfileResponse.class)
				.getBody();
	}
}
