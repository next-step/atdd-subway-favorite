package nextstep.member.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAccessTokenResponse {
	@JsonProperty("access_token")
	private String accessToken;

	private String scope;

	@JsonProperty("token_type")
	private String tokenType;

	public GithubAccessTokenResponse() {
	}

	public GithubAccessTokenResponse(String accessToken, String scope, String tokenType) {
		this.accessToken = accessToken;
		this.scope = scope;
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
