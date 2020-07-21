package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenSecurityContextPersistenceInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final long ID = 1L;

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
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();

        request.addHeader("Authorization", "Bearer NEXTSTEP");
        expected = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }

    @Test
    void preHandler() throws Exception {
        // given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(objectMapper.writeValueAsString(expected));

        // when
        interceptor.preHandle(request, response, mock(Object.class));

        // then
        LoginMember actual = getLoginMember();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(true),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(actual.getAge()).isEqualTo(expected.getAge())
        );

    }

    private LoginMember getLoginMember() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (LoginMember) authentication.getPrincipal();
    }
}
