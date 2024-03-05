package nextstep.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "github")
@ConstructorBinding
public class GithubConfigProperties {
	private final String urlAccessToken;
	private final String urlProfile;
	private final String clientId;
	private final String clientSecret;

	public GithubConfigProperties(String urlAccessToken, String urlProfile, String clientId, String clientSecret) {
		this.urlAccessToken = urlAccessToken;
		this.urlProfile = urlProfile;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getUrlAccessToken() {
		return urlAccessToken;
	}

	public String getUrlProfile() {
		return urlProfile;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
}
