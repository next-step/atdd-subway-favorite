package nextstep.subway.auth.dto;

public interface UserDetails {
    Long getId();

    String getEmail();

    Integer getAge();

    boolean checkPassword(String password);
}
