package nextstep.subway.auth.acceptance.ui.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "test@test.com";
    private static final String REGEX = ":";
    private static final String PASSWORD = "test";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        interceptor = new TokenSecurityContextPersistenceInterceptor();
        objectMapper = new ObjectMapper();

        request.addHeader("authorization", "Bearer jwtToken");
    }

    @DisplayName("토큰 헤더 값 검증")
    @Test
    void validateToken() {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when
        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("SecurityContextHolder 값 검증")
    @Test
    void validateSecurityContextHolder() throws JsonProcessingException {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 1);
        String payload = objectMapper.writeValueAsString(loginMember);

        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(payload);

        // when
        interceptor.preHandle(request, response, new Object());
        LoginMember logged = (LoginMember)getAuthentication().getPrincipal();

        // then
        assertAll(
            () -> assertThat(logged).isNotNull(),
            () -> assertThat(logged.getId()).isEqualTo(1L),
            () -> assertThat(logged.getEmail()).isEqualTo(EMAIL),
            () -> assertThat(logged.getAge()).isEqualTo(PASSWORD)
        );
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
