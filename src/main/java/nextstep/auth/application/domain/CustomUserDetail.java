package nextstep.auth.application.domain;

public interface CustomUserDetail {


    String getId();

    String getPassword();

    boolean checkPassword(String password);
}
