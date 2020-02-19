package atdd.auth.application.dto;

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

  public String toHeaderString() {
    return tokenType + " " + accessToken;
  }

  public AuthInfoView(String headerString) {
    String[] splited = headerString.split(" ");
    this.tokenType = splited[0];
    this.accessToken = splited[1];
  }

  public AuthInfoView() {
  }
}
