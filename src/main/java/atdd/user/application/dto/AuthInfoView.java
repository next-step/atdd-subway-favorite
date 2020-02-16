package atdd.user.application.dto;

public class AuthInfoView {
  String accessToken;
  String tokenType;

  public AuthInfoView(String accessToken, String tokenType) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }
}
