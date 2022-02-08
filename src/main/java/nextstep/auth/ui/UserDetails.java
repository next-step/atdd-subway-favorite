package nextstep.auth.ui;

public interface UserDetails {
    Long getId();

    String getEmail();

    String getPassword();

    Integer getAge();

    boolean checkPassword(String password);
}
