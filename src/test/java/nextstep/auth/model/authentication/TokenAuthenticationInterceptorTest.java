package nextstep.auth.model.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.Authentication;
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

import static nextstep.auth.model.factory.MockServletDataFactory.*;
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
    @DisplayName("AuthenticationToken 을 통해 Authentication 을 구성한다.")
    void authenticate() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest(objectMapper);
        AuthenticationToken authenticationToken = objectMapper.readValue(mockRequest.getInputStream(), AuthenticationToken.class);

        // when
        Authentication authentication = tokenAuthenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((MemberAdaptor) authentication.getPrincipal()).getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(((MemberAdaptor) authentication.getPrincipal()).getAge()).isEqualTo(26);
    }
}