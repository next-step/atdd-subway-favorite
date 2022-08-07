package nextstep.subway.unit.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.service.CustomUserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.MemberRepository;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final int AGE = 20;
	private static final List<String> ROLES = Arrays.asList("ROLE_ADMIN");
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	@Mock
	private CustomUserDetails customUserDetails;
	@Autowired
	private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

	@BeforeEach
	void setUp() {
		customUserDetails = new CustomUserDetailsService(memberRepository);
		tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetails, jwtTokenProvider);
	}

	/*
	@Test
	void convert() throws IOException {

		AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());
		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}

	@Test
	void authenticate() throws IOException {
		//given
		AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));

		//when
		Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

		//then
		assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authentication.getAuthorities()).isEqualTo(ROLES);
	}

	@Test
	void afterAuthenticate() throws IOException {
		//given
		when(jwtTokenProvider.createToken(EMAIL, ROLES)).thenReturn(JWT_TOKEN);
		when(memberRepository.findByEmail(EMAIL))
			.thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE, ROLES)));
		AuthUser authUser = customUserDetails.loadUserByUsername(createTokenRequest().getEmail());

		//when
		String token = jwtTokenProvider.createToken(authUser.getUserName(), authUser.getAuthorities());

		//then
		assertThat(token).isEqualTo(JWT_TOKEN);
	}

	private TokenRequest createTokenRequest() throws IOException {
		String content = createMockRequest().getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		return new ObjectMapper().readValue(content, TokenRequest.class);
	}
*/
	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
		return request;
	}

}
