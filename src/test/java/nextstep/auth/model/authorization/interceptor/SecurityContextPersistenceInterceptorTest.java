package nextstep.auth.model.authorization.interceptor;

import nextstep.auth.model.authentication.service.UserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.context.SecurityContextHolder;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.subway.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.auth.model.factory.MockServletDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SecurityContextPersistenceInterceptorTest {
    private final String TOKEN_REQUEST_HEADER_FILED = "Authorization";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    private SecurityContextPersistenceInterceptor securityContextPersistenceInterceptor;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private MemberAdaptor memberAdaptor;

    @BeforeEach
    void init() {
        mockRequest = createMockRequest();
        mockResponse = createMockResponse();

        Member member = new Member(MOCK_EMAIL, MOCK_PASSWORD, MOCK_AGE);
        memberAdaptor = MemberAdaptor.of(member);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("세션 인증에 대해 SecurityContext 를 영속화한다.")
    void 세션_인증_인증정보_꺼내기() throws Exception {
        // given
        securityContextPersistenceInterceptor = new SessionSecurityContextPersistenceInterceptor();
        요청세션에_인증정보_담기();

        // when
        securityContextPersistenceInterceptor.preHandle(mockRequest, mockResponse, new Object());

        // then
        assertThat(getUsernameFromSecurityContextHolder()).isEqualTo(MOCK_EMAIL);
    }

    private void 요청세션에_인증정보_담기() {
        HttpSession session = mockRequest.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContext.from(new Authentication(memberAdaptor)));
    }

    @Test
    @DisplayName("토큰 인증에 대해 SecurityContext 를 영속화한다.")
    void 토큰_인증_인증정보_꺼내기() throws Exception {
        // given
        securityContextPersistenceInterceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, userDetailsService);
        요청헤더에_토큰정보_담기();

        // when
        securityContextPersistenceInterceptor.preHandle(mockRequest, mockResponse, new Object());

        // then
        assertThat(getUsernameFromSecurityContextHolder()).isEqualTo(MOCK_EMAIL);
    }

    private void 요청헤더에_토큰정보_담기() {
        String jwtToken = jwtTokenProvider.createToken(memberAdaptor.getUsername());
        mockRequest.addHeader(TOKEN_REQUEST_HEADER_FILED, "Bearer " + jwtToken);
    }

    private String getUsernameFromSecurityContextHolder() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().getUsername();
    }
}