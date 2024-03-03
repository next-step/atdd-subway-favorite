package nextstep.auth.application.service;

import nextstep.member.application.dto.MemberRequest;

public interface UserDetailService {
	boolean isNotMember(String email, String password);
	void createMemberIfNotExist(String email, String password, int age);
}
