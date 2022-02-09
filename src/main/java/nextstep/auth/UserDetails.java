package nextstep.auth;

public interface UserDetails {
    Long getId();

    String getEmail();

    String getPassword();

    Integer getAge();

    boolean checkPassword(String password);
}
