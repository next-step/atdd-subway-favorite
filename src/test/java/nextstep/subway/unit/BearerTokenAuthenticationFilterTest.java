package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.chain.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.provider.JwtAuthenticationProvider;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BearerTokenAuthenticationFilterTest {

    private static final Member USER = Member.createUser("user2@gmail.com", "user3", 23);
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJpYXQiOjE2NTk4Njc3NDgsImV4cCI6MTY1OTg3MTM0OCwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXX0.UKyCYJqiIszwNd_9VCOpEaOeiRabG2OGKzSXfYjc2-0";

    @Mock
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @InjectMocks
    BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

    @Test
    void convert() {
        AuthenticationToken authenticationToken = bearerTokenAuthenticationFilter.convert(createMockRequest());

        assertThat(authenticationToken.getCredentials()).isEqualTo(JWT_TOKEN);
    }

    @Test
    void preHandle() throws Exception {
        //given
        when(jwtAuthenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(USER.getEmail(), USER.getRoles()));

        boolean isChain = bearerTokenAuthenticationFilter.preHandle(createMockRequest(), createMockResponse(), new Object());
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        assertThat(isChain).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(USER.getEmail());
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }

    @Test
    void authentication() {
        when(jwtAuthenticationProvider.authenticate(any())).thenReturn(new Authentication(USER.getEmail(), USER.getRoles()));

        Authentication authentication = bearerTokenAuthenticationFilter.authentication(new AuthenticationToken(USER.getEmail(), USER.getPassword()));

        assertThat(authentication.getPrincipal()).isEqualTo(USER.getEmail());
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", AuthorizationType.BEARER.toLowerCase() + JWT_TOKEN);
        return request;
    }

    private MockHttpServletResponse createMockResponse(){
        return new MockHttpServletResponse();
    }

}
