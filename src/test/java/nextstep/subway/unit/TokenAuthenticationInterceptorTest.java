package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";
  @Mock
  private CustomUserDetailsService customUserDetailsService;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

  @BeforeEach
  void setup() {
    ObjectMapper objectMapper = new ObjectMapper();
    tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper);
  }

  @Test
  void convert() throws IOException {
    // when
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());

    // then
    assertAll(
      () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
      () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
    );
  }

  @Test
  void authenticate() throws IOException {
    // given
    createMockLoginMember();
    Authentication targetAuth = new Authentication(customUserDetailsService.loadUserByUsername(EMAIL));
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createMockRequest());

    // when
    Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

    // then
    assertThat(authentication.getPrincipal()).isEqualTo(targetAuth.getPrincipal());
  }

  @Test
  void invalidTokenAuthenticationTest() throws IOException {
    // given
    createMockLoginMember();
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createInvalidMockRequest());

    // when & then
    assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(authenticationToken)).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void preHandle() throws IOException {
    // given
    MockHttpServletRequest mockRequest = createMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    createMockLoginMember();
    when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

    // when
    boolean result = tokenAuthenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

    // then
    assertAll(
      () -> assertThat(result).isFalse(),
      () -> assertThat(mockResponse.getStatus()).isEqualTo(SC_OK),
      () -> assertThat(mockResponse.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)))
    );
  }

  private void createMockLoginMember() {
    // given
    when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 25));
  }

  private MockHttpServletRequest createMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }

  private MockHttpServletRequest createInvalidMockRequest() throws IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    TokenRequest tokenRequest = new TokenRequest(EMAIL, "teststeststestst");
    request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
    return request;
  }

}
