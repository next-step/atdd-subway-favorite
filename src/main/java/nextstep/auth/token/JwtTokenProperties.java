package nextstep.auth.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProperties {

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;
	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;

	private JwtTokenProperties() {
	}

	public JwtTokenProperties(String secretKey, long validityInMilliseconds) {
		this.secretKey = secretKey;
		this.validityInMilliseconds = validityInMilliseconds;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public long getValidityInMilliseconds() {
		return validityInMilliseconds;
	}
}
