package nextstep.auth.application.service;

public interface UserDetailService {
	boolean isNotMember(String email, String password);
	void createMemberIfNotExist(String email, String password, int age);
}
