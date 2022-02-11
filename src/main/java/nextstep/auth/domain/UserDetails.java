package nextstep.auth.domain;

public interface UserDetails {

    String getUsername();

    boolean checkPassword(String password);

}
