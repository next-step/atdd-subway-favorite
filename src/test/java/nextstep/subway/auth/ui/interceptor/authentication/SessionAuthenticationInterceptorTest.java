package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.exception.AuthenticationException;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@DisplayName("Basic 방식 로그인 인터셉터 테스트 ")
@ExtendWith(MockitoExtension.class)
class SessionAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Mock
    private BasicAuthenticationConverter converter;
    @Mock
    private CustomUserDetailsService userDetailsService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AbstractAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        //given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        interceptor = new NewSessionAuthenticationInterceptor(converter, userDetailsService);
    }

    @DisplayName("Basic방식으로 인증 실패시 에러를 던진다.")
    @Test
    void loginFailed() {
        //given
        given(converter.convert(any())).willReturn(new AuthenticationToken(EMAIL, "123"));
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        //when
        assertThatThrownBy(() -> interceptor.preHandle(request, response, mock(Object.class)))
                //then
                .isInstanceOf(AuthenticationException.class);

    }

    @DisplayName("Basic방식으로 인증 성공 시에 액세스 토큰을 응답한다.")
    @Test
    void login() throws Exception {
        //given
        given(converter.convert(any())).willReturn(new AuthenticationToken(EMAIL, PASSWORD));
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

        //when
        boolean result = interceptor.preHandle(request, response, mock(Object.class));

        //then
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        assertThat(request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY)).isNotNull();
    }


}