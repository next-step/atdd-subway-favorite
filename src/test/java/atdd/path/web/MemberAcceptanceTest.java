package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.MemberResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관리")
public class MemberAcceptanceTest extends AbstractAcceptanceTest {

	private MemberHttpTest memberHttpTest;

	@BeforeEach
	void setUp() {
		this.memberHttpTest = new MemberHttpTest(webTestClient);
	}

	@DisplayName("회원 가입")
	@Test
	void createMember() {
		// when
		Long memberId = memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

		// then
		EntityExchangeResult<MemberResponseView> response = memberHttpTest.retrieveMember(memberId);
		assertThat(response.getResponseBody().getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(response.getResponseBody().getName()).isEqualTo(MEMBER_NAME);
	}
}
