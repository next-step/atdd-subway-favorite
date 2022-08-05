package nextstep.auth.domain;

import java.util.List;

public interface AuthUser {
	String getUserName();

	String getPassword();

	List<String> getAuthorities();

	boolean isValidPassword(String password);
}
