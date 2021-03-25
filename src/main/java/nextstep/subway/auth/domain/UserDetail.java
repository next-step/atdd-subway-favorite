package nextstep.subway.auth.domain;

public interface UserDetail {

	String getEmail();

	String getPassword();

	boolean checkPassword(String password);

}
