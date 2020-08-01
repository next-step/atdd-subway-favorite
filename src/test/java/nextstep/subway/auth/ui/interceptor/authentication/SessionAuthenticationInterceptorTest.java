package nextstep.subway.auth.ui.interceptor.authentication;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;
import nextstep.subway.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
public class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "crongro@crong.com";
    private static final String PASSWORD = "crong_plays_football";
    private static final Integer AGE = 20;
    private static final Long ID = 1L;

    @Mock
    private UserDetailService userDetailsService;

    @Mock
    private AuthenticationConverter converter;

    private SessionAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new SessionAuthenticationInterceptor(userDetailsService, converter);
    }

    @DisplayName("세션이 정상적으로 인터셉터 과정에서 prehandle이 실행 되는 지 확인한다.")
    @Test
    void 세션이_정상적으로_인터셉팅_하는지_확인한다() throws IOException {
        // given
        when(converter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUserName(EMAIL)).thenReturn(loginMember);

        // when
        boolean result = interceptor.preHandle(request, response, new Object());
        LoginMember loginMember = getLoginMember();

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(result).isEqualTo(false)
        );

        assertAll(
            () -> assertThat(loginMember.getId()).isEqualTo(ID),
            () -> assertThat(loginMember.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(loginMember.getPassword()).isEqualTo(PASSWORD),
            () -> assertThat(loginMember.getAge()).isEqualTo(AGE)
        );
    }

    private LoginMember getLoginMember() {
        Authentication authentication = getAuthentication();
        return (LoginMember)authentication.getPrincipal();
    }

    private Authentication getAuthentication() {
        SecurityContext context = (SecurityContext)Objects.requireNonNull(request.getSession())
            .getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        return context.getAuthentication();
    }
}
