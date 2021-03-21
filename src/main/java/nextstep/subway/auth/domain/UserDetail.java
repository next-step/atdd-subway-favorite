package nextstep.subway.auth.domain;

public interface UserDetail {

  boolean checkPassword(String password);

  Long getId();

  String getEmail();

  Integer getAge();

  String getPassword();

}
