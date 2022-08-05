package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.member.application.MemberDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetails;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsernamePasswordAuthenticationFilterTest {
    private static final String[] EMAIL = {"email@email.com"};
    private static final String[] PASSWORD = {"password"};
    private static Map<String, String[]> paramMap;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @InjectMocks
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    @Mock
    private MemberDetailsService memberDetailsService;

    @BeforeEach
    void setUp() {
        paramMap = new HashMap<>();
        paramMap.put("username", EMAIL);
        paramMap.put("password", PASSWORD);

        usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter(memberDetailsService);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() {
        //when
        AuthenticationToken token = usernamePasswordAuthenticationFilter.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL[0]);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD[0]);


    }

    @Test
    void authenticate() {
        //given
        MemberDetails memberDetails = MemberDetails.of(new Member(EMAIL[0], PASSWORD[0], 17, List.of(RoleType.ROLE_MEMBER.name())));
        when(memberDetailsService.loadUserByUsername(EMAIL[0]))
                .thenReturn(memberDetails);

        //when
        AuthenticationToken token = new AuthenticationToken(EMAIL[0], PASSWORD[0]);
        Authentication authentication = usernamePasswordAuthenticationFilter.authenticate(token);

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL[0]);
        assertThat(authentication.getAuthorities()).isEqualTo(List.of(RoleType.ROLE_MEMBER.name()));

    }

    @Test
    void preHandle() {
        //given
        MemberDetails memberDetails = MemberDetails.of(new Member(EMAIL[0], PASSWORD[0], 17, List.of(RoleType.ROLE_MEMBER.name())));
        when(memberDetailsService.loadUserByUsername(EMAIL[0]))
                .thenReturn(memberDetails);

        //when
        boolean result = usernamePasswordAuthenticationFilter.preHandle(request, response, null);

        //then
        assertThat(result).isFalse();

    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameters(paramMap);

        return request;
    }


}
