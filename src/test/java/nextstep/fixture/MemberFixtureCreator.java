package nextstep.fixture;

import nextstep.api.member.application.dto.MemberRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */

public class MemberFixtureCreator {

	public static MemberRequest createMemberRequest(String email, String password, int age) {
		return new MemberRequest(email, password, age);
	}

}
