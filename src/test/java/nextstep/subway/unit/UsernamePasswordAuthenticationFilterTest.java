package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.nonchain.UsernamePasswordAuthenticationFilter;
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
class UsernamePasswordAuthenticationFilterTest {

    @Mock
    AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @InjectMocks
    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    Member user = Member.createUser("user1@gmail.com", "pass" , 23);


    @Test
    void convert() {
        //given
        MockHttpServletRequest mockRequest = createMockRequest();

        //when
        AuthenticationToken authenticationToken = usernamePasswordAuthenticationFilter.convert(mockRequest);

        //then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(user.getEmail());
        assertThat(authenticationToken.getCredentials()).isEqualTo(user.getPassword());
    }

    @Test
    void authenticate() {
        //given
        when(authenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(user.getEmail(), user.getRoles()));

        //when
        Authentication authenticate = usernamePasswordAuthenticationFilter.authentication(
                new AuthenticationToken(user.getEmail(), user.getPassword())
        );

        //then
        assertThat(authenticate.getPrincipal()).isEqualTo(user.getEmail());
        assertThat(authenticate.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }

    @Test
    void preHandle() throws Exception {
        //given
        when(authenticationProvider.authenticate(any()))
                .thenReturn(new Authentication(user.getEmail(), user.getRoles()));

        boolean isChain = usernamePasswordAuthenticationFilter.preHandle(createMockRequest(), createMockResponse(), new Object());
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        assertThat(isChain).isFalse();
        assertThat(authentication.getPrincipal()).isEqualTo(user.getEmail());
        assertThat(authentication.getAuthorities()).contains(RoleType.ROLE_MEMBER.name());
    }


    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", user.getEmail());
        request.setParameter("password", user.getPassword());
        return request;
    }

    private MockHttpServletResponse createMockResponse(){
        return new MockHttpServletResponse();
    }

}
