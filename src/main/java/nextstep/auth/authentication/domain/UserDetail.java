package nextstep.auth.authentication.domain;

public interface UserDetail {

    String getEmail();

    boolean checkPassword(String password);
}
