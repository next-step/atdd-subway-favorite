package nextstep.auth.domain;

public interface UserDetail {

    String getEmail();

    String getPassword();

    Integer getAge();

    boolean checkPassword(String password);
}
