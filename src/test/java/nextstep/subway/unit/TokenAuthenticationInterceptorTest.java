package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.UserDetails;

class TokenAuthenticationInterceptorTest {
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	@Test
	void convert() throws IOException {
		CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
		JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
		ObjectMapper objectMapper = new ObjectMapper();

		TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(
			new TokenAuthenticationConverter(objectMapper),
			userDetailsService, jwtTokenProvider);

		AuthenticationToken authenticationToken = interceptor.convert(createMockRequest());

		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}

	@Test
	void authenticate() throws IOException {
		CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
		JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
		MemberRepository memberRepository = mock(MemberRepository.class);

		when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(new Member(EMAIL, PASSWORD, 10)));
		when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(UserDetails.of(new Member(EMAIL, PASSWORD, 10)));

		ObjectMapper objectMapper = new ObjectMapper();

		TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(
			new TokenAuthenticationConverter(objectMapper),
			userDetailsService, jwtTokenProvider);

		AuthenticationToken authenticationToken = interceptor.convert(createMockRequest());

		Authentication authentication = interceptor.authenticate(authenticationToken);

		String json = objectMapper.writeValueAsString(authentication.getPrincipal());
		UserDetails userDetails = objectMapper.readValue(json, UserDetails.class);

		assertThat(userDetails.getEmail()).isEqualTo(EMAIL);
		assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
	}

	@Test
	void preHandle() throws IOException {
		CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
		JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
		ObjectMapper objectMapper = new ObjectMapper();

		TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(
			new TokenAuthenticationConverter(objectMapper),
			userDetailsService, jwtTokenProvider);

		when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new UserDetails(1L, EMAIL, PASSWORD, 20));
		when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

		MockHttpServletRequest request = createMockRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor.preHandle(request, response, new Object());

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		assertThat(response.getContentAsString()).isEqualTo(
			new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContent(new ObjectMapper().writeValueAsString(new TokenRequest(EMAIL, PASSWORD)).getBytes());
		return request;
	}
}
