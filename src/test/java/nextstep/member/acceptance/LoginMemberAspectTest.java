package nextstep.member.acceptance;

import nextstep.auth.AuthenticationException;
import nextstep.auth.LoginMember;
import nextstep.auth.LoginMemberAspect;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.apache.http.HttpHeaders;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class LoginMemberAspectTest {
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private LoginMember loginMember;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private MemberRepository memberRepository;

    private MockHttpServletRequest request;
    private LoginMemberAspect loginMemberAspect;
    private static final String AUTHORIZATION_BEARER = "Bearer ";
    private static final String token = "myToken";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @BeforeEach
    public void setUp() {
        lenient().when(jwtTokenProvider.getPrincipal(token)).thenReturn(EMAIL);
        lenient().when(memberRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        lenient().when(joinPoint.getArgs()).thenReturn(new Object[]{});

        loginMemberAspect = new LoginMemberAspect(jwtTokenProvider, memberRepository);
        request = new MockHttpServletRequest();
    }

    @DisplayName("유효한 토큰인 경우 정상적으로 실행된다")
    @Test
    public void validateTokenAndInjectMember() throws Throwable {
        // given
        request.addHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_BEARER + token);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // when
        Object result = loginMemberAspect.validateTokenAndInjectMember(joinPoint, loginMember);

        // then
        assertThat(result).isNull();
    }

    @DisplayName("유효하지 않은 토큰일 경우 에러가 발생한다")
    @Test
    public void validateTokenAndInjectMember_invalidToken_Exception() {
        // given
        request.addHeader(HttpHeaders.AUTHORIZATION, "");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // when, then
        assertThatThrownBy(() -> loginMemberAspect.validateTokenAndInjectMember(joinPoint, loginMember))
                .isInstanceOf(AuthenticationException.class);
    }
}
