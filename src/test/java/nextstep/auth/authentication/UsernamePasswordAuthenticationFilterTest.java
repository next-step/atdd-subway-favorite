package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.TokenRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernamePasswordAuthenticationFilterTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private UsernamePasswordAuthenticationFilter filter;
    private LoginMemberService loginMemberService;

    @BeforeEach
    void setUp() {
        loginMemberService = mock(LoginMemberService.class);
        filter = new UsernamePasswordAuthenticationFilter(loginMemberService);
    }


    @Test
    void convert() throws IOException {
        var authenticationToken = filter.convert(createMockRequest());

        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    @Test
    void authenticate() {
        when(loginMemberService.loadUserByUsername(EMAIL))
                .thenReturn(new LoginMember(EMAIL, PASSWORD, List.of("ROLE_ADMIN")));

        var authentication = filter.authenticate(new AuthenticationToken(EMAIL, PASSWORD));


        assertAll(
                () -> assertThat(authentication.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authentication.getAuthorities()).containsExactly("ROLE_ADMIN")
        );
    }

    @Test
    void preHandle() throws Exception {
        when(loginMemberService.loadUserByUsername(EMAIL))
                .thenReturn(new LoginMember(EMAIL, PASSWORD, List.of("ROLE_ADMIN")));
        filter.preHandle(createMockRequest(), new MockHttpServletResponse(), null);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).containsExactly("ROLE_ADMIN");
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", EMAIL);
        request.setParameter("password", PASSWORD);
        return request;
    }

}