package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.unit.AuthTarget.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

  @BeforeEach
  void setup() {
    ObjectMapper objectMapper = new ObjectMapper();
    tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper);
  }

  @Test
  void convert() throws IOException {
    // when
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createTokenMockRequest());

    // then
    assertAll(
      () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
      () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
    );
  }

  @Test
  void authenticate() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createTokenMockRequest());

    // when
    Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

    // then
    assertThat(authentication.getPrincipal()).isEqualTo(LOGIN_MEMBER);
  }

  @Test
  void invalidTokenAuthenticationTest() throws IOException {
    // given
    AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.convert(createInvalidMockRequest());

    // when & then
    assertThatThrownBy(() -> tokenAuthenticationInterceptor.authenticate(authenticationToken)).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void preHandle() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    MockHttpServletRequest mockRequest = createTokenMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();
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

}
