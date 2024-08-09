package nextstep.auth.domain;

public interface UserDetails {

    Long getId();
    String getEmail();
    String getPassword();
}
