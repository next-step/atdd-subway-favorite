package atdd.path.application.dto;

public class AccessTokenResponseView {

	private String accessToken;

	public AccessTokenResponseView() {
	}

	public AccessTokenResponseView(final String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public static AccessTokenResponseView of(final String accessToken) {
		return new AccessTokenResponseView(accessToken);
	}
}
