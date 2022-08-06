package nextstep.subway.unit.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.domain.AuthUser;
import nextstep.auth.service.CustomUserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorNextTest {
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 20;
	private static final List<String> ROLES = Arrays.asList("ROLE_ADMIN");
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

	TokenRequest tokenRequest;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private CustomUserDetails customUserDetails;

	@BeforeEach
	void setUp() throws IOException {
		customUserDetails = new CustomUserDetailsService(memberRepository);
		MockHttpServletRequest request = createMockRequest();
		String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
	}

	@Test
	void convert() {
		assertThat(tokenRequest.getEmail()).isEqualTo(EMAIL);
		assertThat(tokenRequest.getPassword()).isEqualTo(PASSWORD);
	}

	@Test
	void authenticate() {
		//given
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));

		//when
		AuthUser authUser = customUserDetails.loadUserByUsername(tokenRequest.getEmail());

		//then
		assertThat(authUser.isValidPassword(PASSWORD)).isTrue();
	}

	@Test
	void afterAuthenticate() {
		//given
		when(jwtTokenProvider.createToken(EMAIL, ROLES))
			.thenReturn(JWT_TOKEN);
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));
		AuthUser authUser = customUserDetails.loadUserByUsername(tokenRequest.getEmail());

		//when
		String token = jwtTokenProvider.createToken(authUser.getUserName(), authUser.getAuthorities());

		//then
		assertThat(token).isEqualTo(JWT_TOKEN);
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
		return request;
	}

}
