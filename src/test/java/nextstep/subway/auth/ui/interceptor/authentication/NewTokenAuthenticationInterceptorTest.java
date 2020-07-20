package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.AuthenticationException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
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

import static nextstep.subway.auth.utils.AuthorizationTestUtils.addBasicAuthorizationHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@DisplayName("Basic 방식 로그인 인터셉터 테스트 ")
@ExtendWith(MockitoExtension.class)
class NewTokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final int AGE = 20;
    private static final String PASSWORD = "password";
    private static final String TOKEN = "accessToken";

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private BasicAuthenticationConverter converter;
    private ObjectMapper objectMapper = new ObjectMapper();
    private NewTokenAuthenticationInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        //given
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        interceptor = new NewTokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper,converter);
    }

    @DisplayName("Basic방식으로 인증 실패시 에러를 던진다.")
    @Test
    void loginFailed() {
        //given
        given(converter.convert(any())).willReturn(new AuthenticationToken(EMAIL, "123"));
        given(customUserDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));

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
        given(customUserDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, AGE));
        given(jwtTokenProvider.createToken(anyString())).willReturn(TOKEN);

        //when
        boolean result = interceptor.preHandle(request, response, mock(Object.class));

        //then
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.getContentAsByteArray()).isEqualTo(objectMapper.writeValueAsBytes(new TokenResponse(TOKEN)));
    }


}