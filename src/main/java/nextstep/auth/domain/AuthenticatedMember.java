package nextstep.auth.domain;

public interface AuthenticatedMember {
	boolean checkPassword(String credentials);
}
