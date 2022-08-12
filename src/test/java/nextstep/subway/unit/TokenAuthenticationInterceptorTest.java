package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.UserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LoginMemberService loginMemberService;

    private TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletResponse createMockResponse() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        return response;
    }

    @Test
    void convert() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequest mockRequest = createMockRequest();
        String content = mockRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        TokenRequest tokenRequest = objectMapper.readValue(content, TokenRequest.class);

        assertThat(tokenRequest.getEmail()).isEqualTo(EMAIL);
        assertThat(tokenRequest.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void authenticate() throws IOException {

    }

    @Test
    void preHandle() throws IOException {

        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, 20, Arrays.asList("ROLE_ADMIN", "ROLE_MEMBER")));
        tokenAuthenticationInterceptor = new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider);
        MockHttpServletRequest mockRequest = createMockRequest();
        MockHttpServletResponse mockResponse = createMockResponse();

        // when
        boolean isFalse = tokenAuthenticationInterceptor.preHandle(mockRequest, mockResponse, null);

        // then
        assertThat(isFalse).isFalse();
        assertThat(mockResponse.getContentAsString()).isNotBlank();
    }

}
