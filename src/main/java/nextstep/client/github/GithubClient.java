package nextstep.client.github;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import nextstep.client.github.dto.GithubAccessTokenRequest;
import nextstep.client.github.dto.GithubAccessTokenResponse;
import nextstep.client.github.dto.GithubUserProfileResponse;
import nextstep.common.yml.GithubConfigCollector;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */
@Component
@RequiredArgsConstructor
public class GithubClient {

	private final GithubConfigCollector githubConfigCollector;
	private final RestTemplate restTemplate;

	/**
	 * 원래의 경우 github url을 실제 "https://github..." 주소로 사용해야 할 것이나
	 * 테스트에서는 fake로 만든 mock url을 사용하여 request 와 response를 stubbing한다
	 * yml 설정에서 가져오는 방식으로 production과 테스트에서 동적으로 다른 url을 가져오도록 설정한다
	 *
	 * @param code
	 * @return
	 */
	public GithubAccessTokenResponse requestGithubToken(String code) {
		return restTemplate
			.exchange(fetchGithubAccessTokenUrl(), POST, createHttpEntity(createGithubRequest(code)), GithubAccessTokenResponse.class)
			.getBody();
	}

	public GithubUserProfileResponse requestGithubUserProfile(String accessToken) {
		return restTemplate
			.exchange(fetchGithubUserProfileUrl(), GET, createHttpEntityWithToken(accessToken), GithubUserProfileResponse.class)
			.getBody();
	}

	private HttpEntity<?> createHttpEntityWithToken(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", String.format("Bearer %s", accessToken));
		return new HttpEntity<>(headers);
	}

	private HttpEntity<MultiValueMap<String, String>> createHttpEntity(GithubAccessTokenRequest accessTokenRequest) {
		return (HttpEntity<MultiValueMap<String, String>>)new HttpEntity(accessTokenRequest, createHeaders());
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", APPLICATION_JSON_VALUE);
		return headers;
	}

	private String fetchGithubAccessTokenUrl() {
		return githubConfigCollector.getGithubAccessTokenUrl();
	}

	private String fetchGithubUserProfileUrl() {
		return githubConfigCollector.getGithubUserUrl();
	}

	private GithubAccessTokenRequest createGithubRequest(String code) {
		String githubClientId = githubConfigCollector.getGithubClientId();
		String githubClientSecret = githubConfigCollector.getGithubClientSecret();
		return GithubAccessTokenRequest.of(code, githubClientId, githubClientSecret);
	}

}
