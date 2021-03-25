package nextstep.subway.auth.dto;

public class SessionRequest {
  private String email;
  private String password;

  public SessionRequest() {
  }

  public SessionRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
