package subway.member;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "github.client")
public class GithubClientProperties {
	private final String id;
	private final String secret;

	public GithubClientProperties(String id, String secret) {
		this.id = id;
		this.secret = secret;
	}

	public String getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}
}
