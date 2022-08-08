package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.user.User;
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

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationFilterTest {

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";

  @InjectMocks
  private BasicAuthenticationFilter basicAuthenticationFilter;

  @Mock
  private UserDetailsService userDetailsService;

  private MockHttpServletRequest request;

  @BeforeEach
  void setUp() {
    request = createMockRequest(EMAIL + ":" + PASSWORD);
  }

  @Test
  void convert() {
    AuthenticationToken authenticationToken = basicAuthenticationFilter.convert(request);

    assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
    assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
  }

  @Test
  void convert_실패() {
    request = createMockRequest(EMAIL);
    assertThatThrownBy(() -> basicAuthenticationFilter.convert(request)).isInstanceOf(AuthenticationException.class);
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
  void createUserDetails_null_에러() {
    given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(null);
    assertThatThrownBy(() -> basicAuthenticationFilter.createUserDetails(new AuthenticationToken(EMAIL, PASSWORD))).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void createUserDetails_비밀번호_같지_않음_에러() {
    User user = new User(EMAIL, PASSWORD, List.of(ROLE_MEMBER.name()));
    given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(user);

    assertThatThrownBy(() -> basicAuthenticationFilter.createUserDetails(new AuthenticationToken(EMAIL, PASSWORD + "123"))).isInstanceOf(AuthenticationException.class);
  }

  private MockHttpServletRequest createMockRequest(String info) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", AuthorizationType.BASIC + " " + Base64.encodeBase64String(info.getBytes()));
    return request;
  }
}