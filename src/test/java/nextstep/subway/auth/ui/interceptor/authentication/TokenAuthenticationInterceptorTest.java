package nextstep.subway.auth.ui.interceptor.authentication;

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
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@DisplayName("Basic 방식 로그인 인터셉터 테스트 ")
@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final int AGE = 20;
    private static final String PASSWORD = "password";
    public static final String REGEX = ":";

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService);
    }

    @DisplayName("Basic방식으로 인증 실패시 401을 응답한다.")
    @Test
    void loginFailed() {
        //given
        given(customUserDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] targetBytes = (EMAIL + REGEX + "123").getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);

        MockHttpServletResponse response = new MockHttpServletResponse();


        //when
        assertThatThrownBy(() -> interceptor.preHandle(request, response, mock(Object.class)))
                //then
                .isInstanceOf(AuthenticationException.class);

    }

    @DisplayName("Basic방식으로 인증 성공 시에 액세스 토큰을 응답한다.")
    @Test
    void login() throws Exception {
        //given
        given(customUserDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] targetBytes = (EMAIL + REGEX + PASSWORD).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean result = interceptor.preHandle(request, response, mock(Object.class));

        //then
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.getContentAsString()).contains("accessToken");

    }
}