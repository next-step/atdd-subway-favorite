package nextstep.subway.auth.application;

public interface UserDetails {
    boolean checkPassword(String password);

    Long getId();

    String getEmail();

    Integer getAge();

    String getPassword();
}
