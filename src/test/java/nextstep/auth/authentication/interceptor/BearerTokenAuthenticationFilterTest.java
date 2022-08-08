package nextstep.auth.authentication.interceptor;

import static nextstep.member.domain.RoleType.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";

  @InjectMocks
  private BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

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
  void createUserDetails_비밀번호_같지_않음_에러() {
    given(jwtTokenProvider.validateToken(anyString())).willReturn(false);

    assertThatThrownBy(() -> bearerTokenAuthenticationFilter.createUserDetails(new AuthenticationToken(EMAIL, PASSWORD + "123"))).isInstanceOf(AuthenticationException.class);
  }
}