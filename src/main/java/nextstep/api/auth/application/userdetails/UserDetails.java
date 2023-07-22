package nextstep.api.auth.application.userdetails;

public interface UserDetails {
    String getUsername();

    String getPassword();

    String getRole();
}
