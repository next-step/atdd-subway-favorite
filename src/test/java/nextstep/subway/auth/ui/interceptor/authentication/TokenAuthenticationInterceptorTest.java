package nextstep.subway.auth.ui.interceptor.authentication;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "javajigi@gmail.com";
    private static final String PASSWORD = "nextstep";
    private static final String WRONG_PASSWORD = "codesquad";
    private static final Integer AGE = 20;
    private static final String REGEX = ":";
    private static final String JWT = "pobiconan";
    private static final Long ID = 1L;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationConverter authenticationConverter;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider,
            authenticationConverter);
    }

    @DisplayName("Basic Auth 로그인을 시도할 때, 토큰 인터셉터의 작동을 테스트한다.")
    @Test
    void 토큰_인터셉터가_동작되어_로그인이_가능하다() throws IOException {
        // given: 회원 등록되어 있음
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);

        // when: 로그인 요청
        addBasicAuthHeader(EMAIL, PASSWORD);
        when(authenticationConverter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT);
        boolean loginResult = tokenAuthenticationInterceptor.preHandle(request, response, mock(Object.class));

        // then: 로그인 처리
        assertAll(
            () -> assertThat(loginResult).isNotNull(),
            () -> assertThat(loginResult).isFalse(),
            () -> assertThat(response.getContentAsByteArray()).isEqualTo(
                objectMapper.writeValueAsBytes(new TokenResponse(JWT)))
        );
    }

    @DisplayName("사용자가 없을 때 로그인을 실패한다.")
    @Test
    void 사용자가_등록되지_않으면_로그인을_실패한다() {
        // given: 사용자가 등록되어 있지 않음
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);

        // when: 로그인 요청
        addBasicAuthHeader(EMAIL, PASSWORD);

        // then: 로그인 처리
        assertThatThrownBy(
            () -> tokenAuthenticationInterceptor.preHandle(request, response, mock(Object.class))
        ).isInstanceOf(RuntimeException.class).hasMessage("there is no user.");
    }

    @DisplayName("비밀번호가 틀리면 로그인을 실패한다.")
    @Test
    void 비밀번호가_틀리면_로그인을_실패한다() {
        // given: 회원 등록되어 있음
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);

        // when: 로그인 요청
        addBasicAuthHeader(EMAIL, WRONG_PASSWORD);

        // then: 로그인 처리
        assertThatThrownBy(
            () -> tokenAuthenticationInterceptor.preHandle(request, response, mock(Object.class))
        ).isInstanceOf(RuntimeException.class).hasMessage("there is no user.");
    }

    private void addBasicAuthHeader(String email, String password) {
        byte[] targetBytes = (email + REGEX + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }

}
