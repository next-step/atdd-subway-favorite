package nextstep.subway.auth.ui.interceptor.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.dto.TokenResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final String REGEX = ":";
    private static final String JWT = "accessToken";
    private static final long ID = 1L;

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
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
        loginMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(customUserDetailsService, jwtTokenProvider, objectMapper);
    }

    @DisplayName("토큰 인터셉터 테스트")
    @Test
    void testPreHandle() throws Exception {
        // given
        addBasicAuthHeader(EMAIL, PASSWORD);
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT);

        // when
        boolean result = tokenAuthenticationInterceptor.preHandle(request, response, mock(Object.class));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).isFalse(),
                () -> assertThat(response.getContentAsByteArray()).isEqualTo(objectMapper.writeValueAsBytes(new TokenResponse(JWT)))
        );
    }


    private void addBasicAuthHeader(String email, String password) {
        byte[] targetBytes = (email + REGEX + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }
}
