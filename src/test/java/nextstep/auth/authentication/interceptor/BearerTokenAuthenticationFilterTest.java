package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";

  @InjectMocks
  private BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = createMockRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void convert() {
    AuthenticationToken authenticationToken = bearerTokenAuthenticationFilter.convert(request);

    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(EMAIL);
  }

  @Test
  void createUserDetails() {
    List<String> roles = List.of(ROLE_MEMBER.name());

    given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
    given(jwtTokenProvider.getPrincipal(anyString())).willReturn(EMAIL);
    given(jwtTokenProvider.getRoles(anyString())).willReturn(roles);

    UserDetails userDetails = bearerTokenAuthenticationFilter.createUserDetails(new AuthenticationToken(EMAIL, PASSWORD));

    assertThat(userDetails.getPrincipal()).isEqualTo(EMAIL);
    assertThat(userDetails.getAuthorities()).isEqualTo(roles);
  }

  @Test
  void preHandle() throws Exception {
    boolean result = bearerTokenAuthenticationFilter.preHandle(request, response, null);

    assertThat(result).isTrue();
  }

  private MockHttpServletRequest createMockRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", AuthorizationType.BEARER + " " + EMAIL);
    return request;
  }
}