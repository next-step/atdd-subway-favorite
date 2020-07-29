package nextstep.subway.auth.infrastructure;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
public class TokenSecurityContextPersistenceInterceptorTest {

    private static final String EMAIL = "javajigi@gmail.com";
    private static final String PASSWORD = "pobiconan";
    private static final Integer AGE = 20;
    private static final Long ID = 1L;

    @Mock
    private UserDetailService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember expectedMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer");
        response = new MockHttpServletResponse();
        expectedMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }

    @DisplayName("올바른 토큰일 때 인터셉터가 정상적으로 회원정보를 반환하는 지 확인한다.")
    @Test
    void 올바른_토큰이_들어오는_경우_회원정보가_조회된다() throws JsonProcessingException {
        // given: 회원이 등록되어 있다.
        // and: 로그인되어있음
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUserName(EMAIL)).thenReturn(expectedMember);

        // when: 내 회원 정보 요청
        interceptor.preHandle(request, response, mock(Object.class));

        // then: 회원 정보가 조회된다.
        LoginMember actualMember = getLoginMember();
        assertAll(
            () -> assertThat(actualMember).isNotNull(),
            () -> assertThat(actualMember.getId()).isEqualTo(expectedMember.getId()),
            () -> assertThat(actualMember.getEmail()).isEqualTo(expectedMember.getEmail()),
            () -> assertThat(actualMember.getPassword()).isEqualTo(expectedMember.getPassword()),
            () -> assertThat(actualMember.getAge()).isEqualTo(expectedMember.getAge())
        );
    }

    @DisplayName("후처리를 진행하면 SecurityContextHolder가 SecurityContext를 제거한다.")
    @Test
    void 후처리가_진행되어_SecurityContext가_제거된다() throws JsonProcessingException {
        // given
        LoginMember loginMember = new LoginMember(1L, "honux77@gmail.com", "honux77", 20);
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

    @DisplayName("올바르지 못한 토큰일 때 인터셉터가 예외를 확인한다.")
    @Test
    void 잘못된_토큰이_들어오는_경우_예외처리를_진행한다() throws JsonProcessingException {
        // given: 로그인되지않음
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        // when: 내 회원 정보 요청
        interceptor.preHandle(request, response, mock(Object.class));

        // then: 인증값이 null로 반환된다.
        assertThat(getAuthentication()).isNull();
    }

    private LoginMember getLoginMember() {
        Authentication authentication = getAuthentication();
        return (LoginMember)authentication.getPrincipal();
    }

    private Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }
}
