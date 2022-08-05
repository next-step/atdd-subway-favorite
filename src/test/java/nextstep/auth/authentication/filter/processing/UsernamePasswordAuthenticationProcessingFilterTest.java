package nextstep.auth.authentication.filter.processing;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
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

class UsernamePasswordAuthenticationProcessingFilterTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private UsernamePasswordAuthenticationProcessingFilter filter;
    private LoginMemberService loginMemberService;

    @BeforeEach
    void setUp() {
        loginMemberService = mock(LoginMemberService.class);
        filter = new UsernamePasswordAuthenticationProcessingFilter(loginMemberService);
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
    void processing() {
        filter.processing(new Authentication(EMAIL, List.of("ROLE_ADMIN")), new MockHttpServletResponse());

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