package nextstep.member.application;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.exception.UnAuthorizationException;

@Component
public class GithubClient {
	private final static String AUTHORIZATION_HEADER_TYPE = "token ";

	@Value("${github.client.id}")
	private String clientId;
	@Value("${github.client.secret}")
	private String clientSecret;
	@Value("${github.url.access-token}")
	private String tokenUrl;
	@Value("${github.url.profile}")
	private String profileUrl;

	public GithubAccessTokenResponse getAccessTokenFromGithub(String code) {
		GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
			code,
			clientId,
			clientSecret
		);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
			githubAccessTokenRequest, headers);

		GithubAccessTokenResponse response = requestGithubAccessToken(httpEntity);

		if (Objects.isNull(response)) {
			throw new UnAuthorizationException();
		}

		String accessToken = response.getAccessToken();
		if (Objects.isNull(accessToken)) {
			throw new UnAuthorizationException();
		}
		return new GithubAccessTokenResponse(accessToken);
	}

	public GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", AUTHORIZATION_HEADER_TYPE + accessToken);

		HttpEntity httpEntity = new HttpEntity<>(headers);

		return requestGithubProfile(httpEntity);
	}

	private GithubAccessTokenResponse requestGithubAccessToken(HttpEntity<MultiValueMap<String, String>> httpEntity) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			return restTemplate
				.exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
				.getBody();
		} catch (HttpClientErrorException e) {
			throw new UnAuthorizationException();
		}
	}

	private GithubProfileResponse requestGithubProfile(HttpEntity httpEntity) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			return restTemplate
				.exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
				.getBody();
		} catch (HttpClientErrorException e) {
			throw new UnAuthorizationException();
		}
	}
}

