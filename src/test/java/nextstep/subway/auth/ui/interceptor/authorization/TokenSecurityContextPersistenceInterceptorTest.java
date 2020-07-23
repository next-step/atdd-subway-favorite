package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final long ID = 1L;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember expected;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        setRequestHeader();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
        expected = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new TokenSecurityContextPersistenceInterceptor(userDetailsService, jwtTokenProvider);
    }

    @Test
    void preHandler() {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(expected);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        LoginMember actual = getLoginMember();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(actual.getAge()).isEqualTo(expected.getAge())
        );
    }

    @DisplayName("유효하지 못한 토큰 테스트")
    @Test
    void invalidToken() {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(getAuthentication()).isNull();
    }

    @DisplayName("afterCompletion은 SecurityContextHolder의 SecurityContext를 제거한다")
    @Test
    void afterCompletionClearsSecurityContext() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 20);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(new ObjectMapper().writeValueAsString(loginMember));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();

        // when
        interceptor.afterCompletion(request, response, new Object(), null);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    private LoginMember getLoginMember() {
        Authentication authentication = getAuthentication();
        return (LoginMember) authentication.getPrincipal();
    }

    private Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    private void setRequestHeader() {
        request.addHeader("AUTHORIZATION", "Bearer ATDD");
    }

}
