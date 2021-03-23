package nextstep.subway.auth.dto;

public interface UserDetail {
    Integer getId();

    String getEmail();

    Integer getAge();

    boolean checkPassword(String password);
}
