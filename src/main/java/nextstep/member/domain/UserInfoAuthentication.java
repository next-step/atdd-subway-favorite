package nextstep.member.domain;

import nextstep.security.domain.Authentication;
import nextstep.security.domain.GrantedAuthority;

public class UserInfoAuthentication implements Authentication<Long, String> {

  private final Long credentials;
  private final String principal;
  private final GrantedAuthority authority;
  private final Boolean authenticated;

  private UserInfoAuthentication(Long credentials, String principal, GrantedAuthority authority,
      Boolean authenticated) {
    this.credentials = credentials;
    this.principal = principal;
    this.authority = authority;
    this.authenticated = authenticated;
  }

  public static UserInfoAuthentication authenticated(Long credentials, String principal,
      GrantedAuthority authority) {
    return new UserInfoAuthentication(credentials, principal, authority, true);
  }

  public static UserInfoAuthentication unauthenticated(Long credentials, String principal,
      GrantedAuthority authority) {
    return new UserInfoAuthentication(credentials, principal, authority, false);
  }

  @Override
  public Long getCredentials() {
    return this.credentials;
  }

  @Override
  public String getPrincipal() {
    return this.principal;
  }

  @Override
  public GrantedAuthority getAuthority() {
    return this.authority;
  }

  @Override
  public boolean isAuthenticated() {
    return this.authenticated;
  }

}
