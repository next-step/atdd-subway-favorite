package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.util.List;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.domain.User;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordAuthenticationFilterTest {

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";
  public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

  @InjectMocks
  private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

  @Mock
  private UserDetailsService userDetailsService;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() throws IOException {
    request = createMockRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void convert() {
    AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.convert(request);

    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
  }

  @Test
  void authenticate() {
    User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));

    Authentication authenticate = usernamePasswordAuthenticationFilter.authenticate(user, new AuthenticationToken(EMAIL, PASSWORD));

    assertThat(authenticate.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticate.getAuthorities()).isEqualTo(List.of(ROLE_MEMBER.name()));
  }

  @Test
  void preHandle() throws Exception {
    User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));
    given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(user);

    boolean result = usernamePasswordAuthenticationFilter.preHandle(request, response, null);

    assertThat(result).isFalse();
  }

  private MockHttpServletRequest createMockRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("username", EMAIL);
    request.addParameter("password", PASSWORD);
    return request;
  }
}