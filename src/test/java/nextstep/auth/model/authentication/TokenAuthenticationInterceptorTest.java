package nextstep.auth.model.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static nextstep.auth.model.authentication.service.MockServletDataFactory.*;
import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TokenAuthenticationInterceptorTest {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @BeforeEach
    void init() {
        Member member = new Member(MOCK_EMAIL, MOCK_PASSWORD, 26);
        memberRepository.save(member);

        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper);
    }

    @Test
    @DisplayName("Request Body 를 통해 AuthenticationToken 을 구성한다.")
    void extractAuthenticationToken() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest(objectMapper);

        // when
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.extractAuthenticationToken(mockRequest);

        // then
        assertThat(authenticationToken.getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(authenticationToken.getPassword()).isEqualTo(MOCK_PASSWORD);
    }

    @Test
    @DisplayName("AuthenticationToken 을 통해 Authentication 을 구성한다.")
    void authenticate() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest(objectMapper);
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.extractAuthenticationToken(mockRequest);

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((MemberAdaptor) authentication.getPrincipal()).getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(((MemberAdaptor) authentication.getPrincipal()).getAge()).isEqualTo(26);
    }

    @Test
    @DisplayName("authentication 을 통해 token 을 추출한다.")
    void extractToken() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest(objectMapper);
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.extractAuthenticationToken(mockRequest);
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // when
        String token = tokenAuthenticationInterceptor.extractJwtToken(authentication);

        // then
        assertThat(jwtTokenProvider.getPayload(token)).isEqualTo(MOCK_EMAIL);
    }

    @Test
    @DisplayName("SecurityContextHolder 에 SecurityContext 를 담는다.")
    void putSecurityContextInSession() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest(objectMapper);
        AuthenticationToken authenticationToken = tokenAuthenticationInterceptor.extractAuthenticationToken(mockRequest);
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // when
        SecurityContext securityContext = new SecurityContext(authentication);
        tokenAuthenticationInterceptor.pushSecurityContextInContextHolder(securityContext);

        // then
        assertThat(SecurityContextHolder.getContext()).isEqualTo(securityContext);
    }
}