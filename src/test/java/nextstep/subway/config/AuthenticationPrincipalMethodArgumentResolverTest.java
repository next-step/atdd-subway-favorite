package nextstep.subway.config;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationPrincipalMethodArgumentResolverTest {
    private AuthenticationPrincipalMethodArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new AuthenticationPrincipalMethodArgumentResolver();
    }

    @DisplayName("supportsParameter는 parameter의 annotation이 AuthenticationPrincipal이고 타입이 LoginMember이면 true를 리턴한다")
    @Test
    void supportsParameterReturnsTrue() {
        // given
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.hasParameterAnnotation(AuthenticationPrincipal.class)).thenReturn(true);
        doReturn(UserDetails.class).when(methodParameter).getParameterType();

        // when
        boolean supports = resolver.supportsParameter(methodParameter);

        // then
        assertThat(supports).isTrue();
    }

    @DisplayName("supportsParameter는 parameter의 annotation이 AuthenticationPrincipal이 아니면 false를 리턴한다")
    @Test
    void supportsParameterReturnsFalse1() {
        // given
        MethodParameter methodParameter = mock(MethodParameter.class);
        doReturn(UserDetails.class).when(methodParameter).getParameterType();

        when(methodParameter.hasParameterAnnotation(AuthenticationPrincipal.class)).thenReturn(false);

        // when
        boolean supports = resolver.supportsParameter(methodParameter);

        // then
        assertThat(supports).isFalse();
    }

    @DisplayName("supportsParameter는 parameter의 타입이 LoginMember가 아니면 false를 리턴한다")
    @Test
    void supportsParameterReturnsFalse2() {
        // given
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.hasParameterAnnotation(AuthenticationPrincipal.class)).thenReturn(false);

        doReturn(Object.class).when(methodParameter).getParameterType();

        // when
        boolean supports = resolver.supportsParameter(methodParameter);

        // then
        assertThat(supports).isFalse();
    }

    @DisplayName("resolveArgument는 SecurityContextHolder에 SecurityContext가 있으면 SecurityContext의 principal을 리턴한다.")
    @Test
    void resolveArgumentReturnsPrincipal() throws Exception {
        // given
        LoginMember principal = new LoginMember(1L, "email@email.com", "password", 20);
        SecurityContextHolder.setContext(new SecurityContext(new Authentication(principal)));

        // when
        Object resolvedArgument = resolver.resolveArgument(null, null, null, null);

        // then
        assertThat(resolvedArgument).isNotNull();
    }

    @DisplayName("resolveArgument는 SecurityContextHolder에 SecurityContext가 없으면 UNAUTHORIZED를 응답한다.")
    @Test
    void resolveArgumentResponsesUNAUTHORIZED() throws Exception {
        // given
        SecurityContextHolder.setContext(new SecurityContext(null));
        NativeWebRequest request = mock(NativeWebRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(request.getNativeResponse(HttpServletResponse.class)).thenReturn(response);

        // when
        Object resolvedArgument = resolver.resolveArgument(null, null, request, null);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(resolvedArgument).isNull();
    }
}