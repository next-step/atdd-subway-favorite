package nextstep.security.domain;

public interface UserInfo<CREDENTIALS, PRINCIPAL> {

  CREDENTIALS getCredentials();

  PRINCIPAL getPrincipal();

}
