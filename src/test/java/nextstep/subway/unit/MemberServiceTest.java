package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

	@Autowired
	MemberService memberService;

	private final String NO_REGISTER_MEMBER_EMAIL = "noregister@email.com";
	private final String NO_REGISTER_MEMBER_TOKEN = "qweqweqwe121t2eg2g23g3gh23h";

	@Test
	void findMemberByEmailAndAccessTokenFromGithub(){
		//when
		MemberResponse response = memberService.findMemberByEmailAndAccessTokenFromGithub(NO_REGISTER_MEMBER_EMAIL, NO_REGISTER_MEMBER_TOKEN);
		MemberResponse memberByEmail = memberService.findMemberByEmail(response.getEmail());
		//then
		Assertions.assertThat(memberByEmail).isNotNull();
	}
}
