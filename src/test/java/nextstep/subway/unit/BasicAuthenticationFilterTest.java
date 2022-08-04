package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.BasicAuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.member.application.MemberDetailsService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetails;
import nextstep.member.domain.RoleType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasicAuthenticationFilterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @InjectMocks
    private BasicAuthenticationFilter basicAuthenticationFilter;

    @Mock
    private MemberDetailsService memberDetailsService;


    @BeforeEach
    void setUp() {
        basicAuthenticationFilter = new BasicAuthenticationFilter(memberDetailsService);
        request = createMockRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void convert() {
        //when
        AuthenticationToken token = basicAuthenticationFilter.convert(request);

        //then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);

    }

    @Test
    void authenticate() {
        //given
        MemberDetails memberDetails = MemberDetails.of(new Member(EMAIL, PASSWORD, 17, List.of(RoleType.ROLE_MEMBER.name())));
        when(memberDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(memberDetails);
        //when
        Authentication authentication = basicAuthenticationFilter.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        //then
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getAuthorities()).isEqualTo(List.of(RoleType.ROLE_MEMBER.name()));

    }

    @Test
    void preHandle() {
        //given
        MemberDetails memberDetails = MemberDetails.of(new Member(EMAIL, PASSWORD, 17, List.of(RoleType.ROLE_MEMBER.name())));
        when(memberDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(memberDetails);

        //when
        boolean result = basicAuthenticationFilter.preHandle(request, response, null);

        //then
        assertThat(result).isTrue();

    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String encodedBasicAuth = new String(Base64.encodeBase64(String.format("%s:%s", EMAIL, PASSWORD).getBytes()));
        request.addHeader("Authorization", "basic " + encodedBasicAuth);
        return request;
    }
}
