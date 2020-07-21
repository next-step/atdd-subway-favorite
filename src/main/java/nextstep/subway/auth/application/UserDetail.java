package nextstep.subway.auth.application;

/**
 * 이게 도메인이라고 하기에는 살짝 이상해서 일단 이 곳에 둠..
 */
public interface UserDetail {

    boolean checkPassword(String password);

    Long getId();

    String getEmail();

    Integer getAge();
}
