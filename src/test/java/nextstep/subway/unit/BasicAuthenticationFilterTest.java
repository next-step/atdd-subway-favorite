package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpHeaders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class BasicAuthenticationFilterTest {
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 20;
	private static final List<String> ROLES = Arrays.asList("ROLE_ADMIN");

	@Mock
	private MemberRepository memberRepository;
	@Autowired
	private LoginMemberService loginMemberService;
	String authHeader;

	@BeforeEach
	void setUp() throws IOException {
		loginMemberService = new LoginMemberService(memberRepository);
		MockHttpServletRequest request = createMockRequest();
		String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
		authHeader = new String(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(authCredentials));

	}

	@Test
	void convert() {
		//when
		String[] splits = authHeader.split(":");

		//then
		Assertions.assertThat(splits[0]).isEqualTo(EMAIL);
		Assertions.assertThat(splits[1]).isEqualTo(PASSWORD);
	}

	@Test
	void authenticate() {
		//given
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));
		String[] splits = authHeader.split(":");
		String principal = splits[0];
		String credentials = splits[1];

		//when
		AuthenticationToken token = new AuthenticationToken(principal, credentials);
		LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

		//then
		assertThat(loginMember.checkPassword(PASSWORD)).isTrue();
	}

	private MockHttpServletRequest createMockRequest() {
		String headerValue = String.format("%s:%s", EMAIL, PASSWORD);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
			headerValue.getBytes()));

		return request;
	}
}
