package nextstep.subway.unit;

import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.chain.BasicAuthenticationFilter;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationFilterTest {

    @Mock
    UserDetailsService userDetailsService;

    @InjectMocks
    BasicAuthenticationFilter basicAuthenticationFilter;

    static final String TOKEN = "Basic cGFya3VyYW0xMkBnbWFpbC5jb206cGFzcw==";

    private static final Member ADMIN = Member.createAdmin("parkuram12@gmail.com", "pass", 25);

    @Test
    void preHandle() {
        //given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(ADMIN.getEmail(), ADMIN.getPassword(), ADMIN.getRoles()));
        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = createMockResponse();

        //when
        boolean isChain = basicAuthenticationFilter.preHandle(request, response, new Object());
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        //then
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
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(ADMIN.getEmail(), ADMIN.getPassword(), ADMIN.getRoles()));

        //when
        Authentication authentication = basicAuthenticationFilter.authenticate(
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

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }

}
