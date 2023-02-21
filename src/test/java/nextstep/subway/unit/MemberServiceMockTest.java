package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceMockTest {

	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 24;
	private static final List<String> ROLES = new ArrayList<>() {{
		add(RoleType.ROLE_ADMIN.name());
	}};

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private MemberService memberService;

	@Test
	@DisplayName("토큰으로 부터 멤버 정보 가져오기")
	void getInfoFromToken() {
		Member member = new Member(EMAIL, PASSWORD, AGE, ROLES);
		given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

		// when
		MemberResponse memberInfo = memberService.findMemberOfMine("토큰값");

		// then
		assertThat(memberInfo.getEmail()).isEqualTo(EMAIL);
	}
}
