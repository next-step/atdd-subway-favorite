package nextstep.subway.unit;

import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.nonchain.UsernamePasswordAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernamePasswordAuthenticationFilterTest {

    @Mock
    UserDetailsService userDetailsService;

    LoginMember loginMember = new LoginMember(
            "parkuram12@gmail.com",
            "password"
            ,List.of(RoleType.ROLE_MEMBER.name())
    );


    @InjectMocks
    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    @Test
    void preHandle() {
        //given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(loginMember.getUsername(), loginMember.getPassword(), loginMember.getAuthorities()));
        MockHttpServletRequest mockRequest = createMockRequest();
        MockHttpServletResponse mockResponse = createMockResponse();

        //when
        boolean isChain = usernamePasswordAuthenticationFilter.preHandle(mockRequest, mockResponse, new Object());
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        //then
        assertThat(isChain).isFalse();
        assertThat(authentication.getPrincipal()).isEqualTo(loginMember.getUsername());
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }

    @Test
    void convert() {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.convert(mockRequest);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(loginMember.getUsername());
        assertThat(authenticationToken.getCredentials()).isEqualTo(loginMember.getPassword());
    }

    @Test
    void authenticate() {
        //given
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(loginMember.getUsername(), loginMember.getPassword(), loginMember.getAuthorities()));

        //when
        Authentication authenticate = usernamePasswordAuthenticationFilter.authenticate(
                new AuthenticationToken(loginMember.getUsername(), loginMember.getPassword())
        );

        //then
        assertThat(authenticate.getPrincipal()).isEqualTo(loginMember.getUsername());
        assertThat(authenticate.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", loginMember.getUsername());
        request.setParameter("password", loginMember.getPassword());
        return request;
    }

    private MockHttpServletResponse createMockResponse() {
        return new MockHttpServletResponse();
    }


}
