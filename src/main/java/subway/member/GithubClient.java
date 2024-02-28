package subway.member;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import subway.dto.member.GithubAccessTokenRequest;
import subway.dto.member.GithubAccessTokenResponse;
import subway.dto.member.GithubProfileResponse;

@Component
public class GithubClient {
	// @Value("${github.client.id}")
	// private String clientId;
	//
	// @Value("${github.client.secret}")
	// private String clientSecret;
	//
	// @Value("${github.url.access-token}")
	// private String accessTokenUrl;
	//
	// @Value("${github.url.profile}")
	// private String profileUrl;

	private final GithubUrlProperties githubUrlProperties;

	private final GithubClientProperties githubClientProperties;

	public GithubClient(GithubUrlProperties githubUrlProperties, GithubClientProperties githubClientProperties) {
		this.githubUrlProperties = githubUrlProperties;
		this.githubClientProperties = githubClientProperties;
	}

	public String requestGithubToken(String code) {
		GithubAccessTokenRequest githubAccessTokenRequest =
			new GithubAccessTokenRequest(code, githubClientProperties.getId(), githubClientProperties.getSecret());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(githubAccessTokenRequest, headers);
		RestTemplate restTemplate = new RestTemplate();

		return Objects.requireNonNull(restTemplate
				.exchange(githubUrlProperties.getAccessToken(), HttpMethod.POST, httpEntity,
					GithubAccessTokenResponse.class)
				.getBody())
			.getAccessToken();
	}

	public GithubProfileResponse requestUser(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, String.format("%s %s", "Bearer", accessToken));

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(headers);
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate
			.exchange(githubUrlProperties.getProfile(), HttpMethod.GET, httpEntity, GithubProfileResponse.class)
			.getBody();
	}
}
