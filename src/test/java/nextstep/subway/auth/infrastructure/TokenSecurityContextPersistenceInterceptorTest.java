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

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
public class TokenSecurityContextPersistenceInterceptorTest {

    private static final String EMAIL = "javajigi@gmail.com";
    private static final String PASSWORD = "pobiconan";
    private static final Integer AGE = 20;
    private static final Long ID = 1L;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;
    private ObjectMapper objectMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private LoginMember expectedMember;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer");
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
        expectedMember = new LoginMember(ID, EMAIL, PASSWORD, AGE);
        interceptor = new TokenSecurityContextPersistenceInterceptor(customUserDetailsService, jwtTokenProvider);
    }

    @DisplayName("성공하는 토큰 테스트")
    @Test
    void preHandlerTest() {
        // given: 회원이 등록되어 있다.
        // and: 로그인되어있음
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(EMAIL);
        when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedMember);

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

    private LoginMember getLoginMember() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (LoginMember)authentication.getPrincipal();
    }
}
