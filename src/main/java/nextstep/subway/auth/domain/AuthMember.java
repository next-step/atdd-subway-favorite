package nextstep.subway.auth.domain;

public interface AuthMember {

    boolean checkPassword(String password);

    Long getId();

    String getEmail();

    Integer getAge();

    String getPassword();
}
