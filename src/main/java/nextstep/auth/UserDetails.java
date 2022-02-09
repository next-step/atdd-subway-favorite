package nextstep.auth;

public interface UserDetails {

    boolean checkPassword(String password);

    Long getId();

    String getEmail();

}
