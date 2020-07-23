package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("세션 이용한 SecurityContext persist 테스트")
class SessionSecurityContextPersistenceInterceptorTest {

    private AbstractSecurityContextPersistenceInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        interceptor = new SessionSecurityContextPersistenceInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("세션에서 LoginMember를 불러와서 SecurityContextHolder에 저장한다")
    void setSessionLoginMemberToSecurityContext() {
        //given
        MockHttpSession session = new MockHttpSession();
        LoginMember loginMember = new LoginMember(1L, "email@email.com", "password", 20);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(new Authentication(loginMember)));
        request.setSession(session);

        //when
        boolean preHandle = interceptor.preHandle(request, response, new Object());

        //then
        assertThat(preHandle).isTrue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember);
    }

    @Test
    @DisplayName("세션에 값이 존재하지 않으면 SecurityContextHolder에 저장하지 않는다")
    void noSessionAttribute() {
        //given
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);


        //when
        boolean preHandle = interceptor.preHandle(request, response, new Object());

        //then
        assertThat(preHandle).isTrue();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    @DisplayName("afterCompletion 호출 시 SecurityContextHolder가 초기화 된다")
    void initSecurityContextHolder() {
        //given
        LoginMember mockMember = mock(LoginMember.class);
        SecurityContextHolder.setContext(new SecurityContext(new Authentication(mockMember)));

        //when
        interceptor.afterCompletion(request, response, new Object(), null);

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}