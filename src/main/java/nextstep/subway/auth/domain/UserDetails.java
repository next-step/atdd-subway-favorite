package nextstep.subway.auth.domain;

public interface UserDetails {

    Long getId();
    String getEmail();
    String getPassword();
    Integer getAge();

    boolean validatePassword(String password);

}
