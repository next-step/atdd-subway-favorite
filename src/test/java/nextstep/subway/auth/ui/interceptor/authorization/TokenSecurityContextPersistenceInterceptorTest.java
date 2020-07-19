package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.util.ConvertUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("Bearer 인증을 수행하는 인터셉터 테스트")
@ExtendWith(MockitoExtension.class)
class TokenSecurityContextPersistenceInterceptorTest {

    private static final String EMAIL = "high.neoul@gmail.com";
    private static final int AGE = 1;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private TokenSecurityContextPersistenceInterceptor interceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
        request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer abcdefg");
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle() throws Exception {

        // stubbing
        final MemberResponse memberResponse = new MemberResponse(1L, EMAIL, AGE);
        final String json = ConvertUtils.stringify(memberResponse);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPayload(anyString())).thenReturn(json);

        // when
        interceptor.preHandle(request, response, null);

        // then
        final SecurityContext context = SecurityContextHolder.getContext();
        final LoginMember loginMember = (LoginMember) context.getAuthentication().getPrincipal();
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getAge()).isEqualTo(AGE);
    }
}