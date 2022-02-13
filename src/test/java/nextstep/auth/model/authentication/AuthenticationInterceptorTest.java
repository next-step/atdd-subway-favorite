package nextstep.auth.model.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.factory.MockServletDataFactory;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static nextstep.auth.model.factory.MockServletDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthenticationInterceptorTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthenticationInterceptor sessionAuthenticationInterceptor;
    @Autowired
    private AuthenticationInterceptor tokenAuthenticationInterceptor;
    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticationInterceptor authenticationInterceptor;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private SecurityContext securityContext;

    @BeforeEach
    void init() {
        memberRepository.save(new Member(MOCK_EMAIL, MOCK_PASSWORD, MOCK_AGE));

        mockResponse = MockServletDataFactory.createMockResponse();
    }

    @Test
    @DisplayName("세션 기반 인증 interceptor 단위 테스트")
    void 세션_인증() throws IOException {
        // given
        authenticationInterceptor = sessionAuthenticationInterceptor;
        mockRequest = createSessionMockRequest();

        // when
        인증을_수행한다();

        // then
        MemberAdaptor memberAdaptor = 세션으로부터_인증정보를_불러온다();

        assertThat(memberAdaptor.getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(memberAdaptor.getAge()).isEqualTo(MOCK_AGE);

        assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    private MemberAdaptor 세션으로부터_인증정보를_불러온다() {
        SecurityContext securityContext = (SecurityContext) mockRequest.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        MemberAdaptor memberAdaptor = (MemberAdaptor) securityContext.getAuthentication().getPrincipal();

        return memberAdaptor;
    }

    @Test
    @DisplayName("토큰 기반 인증 interceptor 단위 테스트")
    void 토큰_인증() throws IOException {
        // given
        authenticationInterceptor = tokenAuthenticationInterceptor;
        mockRequest = createTokenMockRequest(objectMapper);

        // when
        인증을_수행한다();

        // then
        MemberAdaptor memberAdaptor = 토큰으로부터_인증정보를_불러온다();

        assertThat(memberAdaptor.getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(memberAdaptor.getAge()).isEqualTo(MOCK_AGE);

        assertThat(mockResponse.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    private MemberAdaptor 토큰으로부터_인증정보를_불러온다() {
        return null;
    }

    private boolean 인증을_수행한다() throws IOException {
        return authenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());
    }
}