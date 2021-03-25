package nextstep.subway.auth.domain;

public interface UserDetail {
    Long getId();
    String getEmail();
    String getPassword();
    Integer getAge();
    boolean checkPassword(String password);
}
