package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.interceptor.UsernamePasswordAuthenticationFilter;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordAuthenticationFilterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final UserDetails ADMIN_USER = new LoginMember(EMAIL, PASSWORD, List.of(ROLE_ADMIN.name()));

    @Mock
    private UserDetailsService userDetailsService;

    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    @BeforeEach
    void setUp() {
        this.usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter(userDetailsService);
    }

    @DisplayName("토큰 변환 검증")
    @Test
    void convert() {
        // when
        AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.getAuthenticationToken(createMockRequest());

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Authentication 생성")
    @Test
    void authenticate() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(ADMIN_USER);

        AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.getAuthenticationToken(createMockRequest());

        // when
        Authentication authentication = usernamePasswordAuthenticationFilter.getAuthentication(authenticationToken);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).containsExactly(ROLE_ADMIN.name());
    }

    @DisplayName("username & password 인터셉터 검증")
    @Test
    void preHandle() {
        // given
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(ADMIN_USER);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        usernamePasswordAuthenticationFilter.preHandle(createMockRequest(), response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }

}
