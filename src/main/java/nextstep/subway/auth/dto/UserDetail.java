package nextstep.subway.auth.dto;

public interface UserDetail {
    Long getId();

    String getEmail();

    Integer getAge();

    boolean checkPassword(String password);
}
