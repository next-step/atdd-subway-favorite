package nextstep.security.domain;

public interface Authentication<CREDENTIALS, PRINCIPAL> {

  CREDENTIALS getCredentials();

  PRINCIPAL getPrincipal();

  GrantedAuthority getAuthority();

  boolean isAuthenticated();

}
