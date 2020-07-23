package nextstep.subway.auth.ui.jwt;

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
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.jwt.TokenAuthenticationInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "javajigi@gmail.com";
    private static final String PASSWORD = "nextstep";
    private static final Integer AGE = 20;
    private static final String REGEX = ":";
    private static final String JWT = "pobiconan";
    private static final Long ID = 1L;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember loginMember;

    @BeforeEach
    private void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider);
    }

    @DisplayName("Basic Auth 로그인을 시도할 때, 토큰 인터셉터의 작동을 테스트한다.")
    @Test
    void 토큰_인터셉터가_동작되어_로그인이_가능하다() throws IOException {
        // given: 회원 등록되어 있음
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);

        // when: 로그인 요청
        addBasicAuthHeader(EMAIL, PASSWORD);
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

    private void addBasicAuthHeader(String email, String password) {
        byte[] targetBytes = (email + REGEX + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }

}
