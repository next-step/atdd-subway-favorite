package nextstep.auth.application;

public interface UserDetails {
    Long getId();
    String getEmail();
    String getPassword();
    Integer getAge();
}
