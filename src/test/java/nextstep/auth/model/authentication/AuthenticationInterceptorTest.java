package nextstep.auth.model.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.factory.MockServletDataFactory;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.auth.model.token.dto.TokenResponse;
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
import java.io.UnsupportedEncodingException;

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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    private AuthenticationInterceptor authenticationInterceptor;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;

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

    private MemberAdaptor 토큰으로부터_인증정보를_불러온다() throws UnsupportedEncodingException, JsonProcessingException {
        TokenResponse token = objectMapper.readValue(mockResponse.getContentAsString(), TokenResponse.class);
        String email = jwtTokenProvider.getPayload(token.getAccessToken());

        return userDetailsService.loadUserByUsername(email);
    }

    private boolean 인증을_수행한다() throws IOException {
        return authenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());
    }
}