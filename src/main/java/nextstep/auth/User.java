package nextstep.auth;

public interface User {
    boolean checkPassword(String password);

    Long getId();

    String getEmail();

    Integer getAge();

    String getPassword();
}
