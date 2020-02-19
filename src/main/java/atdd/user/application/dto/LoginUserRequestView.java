package atdd.user.application.dto;

public class LoginUserRequestView {
  String email;
  String password;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public LoginUserRequestView(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public LoginUserRequestView() {
  }
}
