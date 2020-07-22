package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenSecurityContextPersistenceInterceptorTest {
    private JwtTokenProvider jwtTokenProvider;
    private TokenSecurityContextPersistenceInterceptor interceptor;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }

    @DisplayName("preHandle은 JwtToken이 유효하면 SecurityContext를 SecurityContextHolder에 담는다")
    @Test
    void preHandleWhenJwtTokenIsValid() throws Exception {
        // given
        UserDetails userDetails = new UserDetails(1L, "email@email.com", "password", 20);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(new ObjectMapper().writeValueAsString(userDetails));

        // when
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        // then
        UserDetails principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assertThat(principal.getId()).isEqualTo(userDetails.getId());
    }

    @DisplayName("preHandle은 JwtToken이 유효하지 않으면 SecurityContext를 SecurityContextHolder에 담지 않는다")
    @Test
    void preHandleWhenJwtTokenIsNotValidSecurityContext() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @DisplayName("afterCompletion은 SecurityContextHolder의 SecurityContext를 제거한다")
    @Test
    void afterCompletionClearsSecurityContext() throws Exception {
        // given
        UserDetails userDetails = new UserDetails(1L, "email@email.com", "password", 20);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(new ObjectMapper().writeValueAsString(userDetails));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();

        // when
        interceptor.afterCompletion(request, response, new Object(), null);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}