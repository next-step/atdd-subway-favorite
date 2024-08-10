package nextstep.member.application.dto;

import nextstep.security.domain.UserInfo;

public class UserInfoDto implements UserInfo {

  private final String credentials;
  private final String principal;

  public UserInfoDto(String email, String password) {
    this.credentials = email;
    this.principal = password;
  }

  @Override
  public String getCredentials() {
    return credentials;
  }

  @Override
  public String getPrincipal() {
    return principal;
  }
}
