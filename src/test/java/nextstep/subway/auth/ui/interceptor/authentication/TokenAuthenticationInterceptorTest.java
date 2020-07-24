package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.convert.AuthenticationConverter;
import nextstep.subway.member.application.UserDetailsService;
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
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationConverter converter;

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
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, converter);
    }

    @DisplayName("토큰 인터셉터 테스트")
    @Test
    void preHandle() throws Exception {
        // given
        addBasicAuthHeader(EMAIL, PASSWORD);
        when(converter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT);

        // when
        boolean result = tokenAuthenticationInterceptor.preHandle(request, response, new Object());

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
