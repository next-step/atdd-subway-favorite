package nextstep.config;

import nextstep.config.stub.StubNativeWebRequest;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.LoginUser;
import nextstep.member.domain.exception.IllegalAccessTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class AuthorizationResolverTest {

    private static final String USER_ID = "10001";

    private AuthorizationResolver authorizationResolver;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("test", 10000L);
        authorizationResolver = new AuthorizationResolver(jwtTokenProvider);
    }

    /**
     * When 인증 요청 시
     * Then 인증이 된다
     */
    @DisplayName("인증 헤더로 인증을 받을 수 있다")
    @Test
    void 인증_헤더로_인증을_받을_수_있다() {
        // Given
        String token = jwtTokenProvider.createToken(USER_ID, List.of("manager"));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // When
        LoginUser actual = (LoginUser) authorizationResolver.resolveArgument(any(), any(), new StubNativeWebRequest(mockRequest), any());

        // Then
        assertThat(actual.getUserId().toString()).isEqualTo(USER_ID);
    }

    /**
     * When && Then 인증 헤더 없이 인증 요청 시 인증을 받을 수 없다
     */
    @DisplayName("인증 헤더 없이 인증 요청 시 인증을 받을 수 없다")
    @Test
    void 인증_헤더_없이_인증_요청_시_인증을_받을_수_없다() {
        // When && Then
        assertThatThrownBy(() -> authorizationResolver.resolveArgument(any(), any(), new StubNativeWebRequest(new MockHttpServletRequest()), any()))
                .isInstanceOf(IllegalAccessTokenException.class)
                .hasMessage("잘못된 AccessToken 입니다.");
    }


    /**
     * Given 잘못된 시크릿으로 토큰 생성
     * When && Then 다른 시크릿으로 만들어진 토큰으로 인증 요청 시 인증을 받을 수 없다
     */
    @DisplayName("다른 시크릿으로 만들어진 토큰으로 인증 요청 시 인증을 받을 수 없다")
    @Test
    void 다른_시크릿으로_만들어진_토큰으로_인증_요청_시_인증을_받을_수_없다() {
        // Given
        JwtTokenProvider wrongTokenProvider = new JwtTokenProvider("wrongSecret", 10000L);
        String givenToken = wrongTokenProvider.createToken(USER_ID, List.of("manager"));
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + givenToken);

        // When && Then
        assertThatThrownBy(() -> authorizationResolver.resolveArgument(any(), any(), new StubNativeWebRequest(new MockHttpServletRequest()), any()))
                .isInstanceOf(IllegalAccessTokenException.class)
                .hasMessage("잘못된 AccessToken 입니다.");
    }


}