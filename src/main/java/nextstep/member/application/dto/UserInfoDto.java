package nextstep.member.application.dto;

import nextstep.security.domain.UserInfo;

public class UserInfoDto implements UserInfo {

  private final String credentials;
  private final String principal;

  public UserInfoDto(String credentials, String principal) {
    this.credentials = credentials;
    this.principal = principal;
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
