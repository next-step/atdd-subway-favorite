package nextstep.common.yml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Component
@RequiredArgsConstructor
@Getter
public class GithubConfigCollector {

	@Value("${github.client.id}")
	private String githubClientId;

	@Value("${github.client.secret}")
	private String githubClientSecret;

	@Value("${github.url.access-token}")
	private String githubAccessTokenUrl;

	@Value("${github.url.user}")
	private String githubUserUrl;

}
