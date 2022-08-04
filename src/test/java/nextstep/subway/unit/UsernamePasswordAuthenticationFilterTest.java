package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordAuthenticationFilterTest {
	private static final String PRINCIPAL_NAME = "username";
	private static final String CREDENTIAL_NAME = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 20;
	private static final List<String> ROLES = Arrays.asList("ROLE_ADMIN");

	@Mock
	private MemberRepository memberRepository;
	@Autowired
	private LoginMemberService loginMemberService;
	HttpServletRequest request;

	@BeforeEach
	void setUp() {
		loginMemberService = new LoginMemberService(memberRepository);
		request = createMockRequest();
	}

	@Test
	void convert() {
		//when
		Map<String, String[]> parameterMap = request.getParameterMap();
		String userName = parameterMap.get(PRINCIPAL_NAME)[0];
		String password = parameterMap.get(CREDENTIAL_NAME)[0];

		//then
		Assertions.assertThat(userName).isEqualTo(EMAIL);
		Assertions.assertThat(password).isEqualTo(PASSWORD);
	}

	@Test
	void authenticate() {
		//given
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));
		Map<String, String[]> parameterMap = request.getParameterMap();
		String userName = parameterMap.get(PRINCIPAL_NAME)[0];
		String password = parameterMap.get(CREDENTIAL_NAME)[0];

		//when
		AuthenticationToken token = new AuthenticationToken(userName, password);
		LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

		//then
		assertThat(loginMember.checkPassword(PASSWORD)).isTrue();
	}

	private MockHttpServletRequest createMockRequest() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(PRINCIPAL_NAME, EMAIL);
		request.addParameter(CREDENTIAL_NAME, PASSWORD);

		return request;
	}
}
