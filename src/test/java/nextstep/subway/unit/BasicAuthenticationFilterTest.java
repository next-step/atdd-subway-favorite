package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.chain.BasicAuthenticationFilter;
import nextstep.auth.authentication.provider.AuthenticationProvider;
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
class BasicAuthenticationFilterTest {

    @Mock
    AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @InjectMocks
    BasicAuthenticationFilter basicAuthenticationFilter;

    static final String TOKEN = "Basic cGFya3VyYW0xMkBnbWFpbC5jb206cGFzcw==";

    private static final Member ADMIN = Member.createAdmin("parkuram12@gmail.com", "pass", 25);

    @Test
    void preHandle() throws Exception {
        //given
        when(authenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(ADMIN.getEmail(), ADMIN.getRoles()));

        boolean isChain = basicAuthenticationFilter.preHandle(createMockRequest(), createMockResponse(), new Object());
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        assertThat(isChain).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(ADMIN.getEmail());
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_ADMIN.name());
    }

    @Test
    void convert() {
        //given
        MockHttpServletRequest mockHttpServletRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = basicAuthenticationFilter.convert(mockHttpServletRequest);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(ADMIN.getEmail());
        assertThat(authenticationToken.getCredentials()).isEqualTo(ADMIN.getPassword());
    }

    @Test
    void authenticate() {
        //given
        when(authenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(ADMIN.getEmail(), ADMIN.getRoles()));

        //when
        Authentication authentication = basicAuthenticationFilter.authentication(
                new AuthenticationToken(
                        ADMIN.getEmail(),
                        ADMIN.getPassword()
                )
        );

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(ADMIN.getEmail());
        assertThat(authentication.getAuthorities()).isEqualTo(ADMIN.getRoles());
    }

    private MockHttpServletRequest createMockRequest(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", TOKEN);
        return request;
    }

    private MockHttpServletResponse createMockResponse(){
        return new MockHttpServletResponse();
    }

}
