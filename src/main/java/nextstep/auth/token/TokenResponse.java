package nextstep.auth.token;

import java.util.Objects;

public class TokenResponse {
	private String accessToken;

	public TokenResponse() {
	}

	public TokenResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TokenResponse)) return false;
		TokenResponse that = (TokenResponse) o;
		return Objects.equals(accessToken, that.accessToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken);
	}
}
