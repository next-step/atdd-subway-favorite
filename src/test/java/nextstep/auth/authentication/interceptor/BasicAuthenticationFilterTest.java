package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.member.domain.User;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationFilterTest {

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";

  @InjectMocks
  private BasicAuthenticationFilter basicAuthenticationFilter;

  @Mock
  private UserDetailsService userDetailsService;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = createMockRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void convert() {
    AuthenticationToken authenticationToken = basicAuthenticationFilter.convert(request);

    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
  }

  @Test
  void createUserDetails() {
    User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));
    given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(user);
    UserDetails userDetails = basicAuthenticationFilter.createUserDetails(new AuthenticationToken(EMAIL, PASSWORD));

    assertThat(userDetails.getPrincipal()).isEqualTo(EMAIL);
    assertThat(userDetails.getAuthorities()).isEqualTo(List.of(ROLE_MEMBER.name()));
  }

  @Test
  void preHandle() throws Exception {
    User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));
    given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(user);
    boolean result = basicAuthenticationFilter.preHandle(request, response, null);

    assertThat(result).isTrue();
  }

  private MockHttpServletRequest createMockRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    String input = EMAIL + ":" + PASSWORD;
    request.addHeader("Authorization", AuthorizationType.BASIC + " " + Base64.encodeBase64String(input.getBytes()));
    return request;
  }
}