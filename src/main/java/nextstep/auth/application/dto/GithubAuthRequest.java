package nextstep.auth.application.dto;

public class GithubAuthRequest {
	private String clientId;
	private String clientSecret;
	private String code;

	public GithubAuthRequest() {
	}

	public GithubAuthRequest(String clientId, String clientSecret, String code) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.code = code;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getCode() {
		return code;
	}
}
