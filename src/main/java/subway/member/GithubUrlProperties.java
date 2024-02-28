package subway.member;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github.url")
public class GithubUrlProperties {
	private final String accessToken;
	private final String profile;

	public GithubUrlProperties(String accessToken, String profile) {
		this.accessToken = accessToken;
		this.profile = profile;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getProfile() {
		return profile;
	}
}
