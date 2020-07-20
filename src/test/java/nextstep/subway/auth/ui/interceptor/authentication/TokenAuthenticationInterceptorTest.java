package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("토큰 인증 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password1234";

    private TokenAuthenticationInterceptor interceptor;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(jwtTokenProvider, userDetailsService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("BASIC 인증 스킴이 없을시 RuntimeException")
    @Test
    void convert_withoutHeader() {
       assertThatThrownBy(() -> interceptor.convert(request))
               .isInstanceOf(RuntimeException.class);
    }


    @DisplayName("HttpServletRequest에서 로그인 정보 추출하여 검증 객체 AuthenticationToken 생성")
    @Test
    void convert() {
        // given
        addAuthorizationHeader(request, EMAIL, PASSWORD);

        // when
        AuthenticationToken token = interceptor.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("유저 정보가 없으면 RuntimeException")
    @Test
    void authenticate_noUserDetails() {
        // given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(null);

        // when
        assertThatThrownBy(() -> interceptor.authenticate(token))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("패스워드가 일치하지 않으면 RuntimeException")
    @Test
    void authenticate_notEqualPassword() {
        // given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, "notequalpassword", 10));

        // when
        assertThatThrownBy(() -> interceptor.authenticate(token))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("AuthenticationToken을 통해 인증을 시도하면 인증 객체 Authentication 생성")
    @Test
    void authenticate() {
        // given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        // when
        Authentication authentication = interceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @DisplayName("TokenResponse를 응답한다")
    @Test
    void response() throws Exception {
        // given
        String expectedToken = "token";
        addAuthorizationHeader(request, EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(expectedToken);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(expectedToken)));
    }

    private void addAuthorizationHeader(MockHttpServletRequest request, String email, String password) {
        byte[] bytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        String authorization = new String(encodedBytes);
        request.addHeader("Authorization", "BASIC " + authorization);
    }
}
