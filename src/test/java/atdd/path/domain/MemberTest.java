package atdd.path.domain;

import org.junit.jupiter.api.Test;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

	@Test
	void createMember() {
		// when
		Member member = new Member(MEMBER_ID, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

		// then
		assertThat(member.getId()).isEqualTo(MEMBER_ID);
		assertThat(member.getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(member.getName()).isEqualTo(MEMBER_NAME);
	}
}