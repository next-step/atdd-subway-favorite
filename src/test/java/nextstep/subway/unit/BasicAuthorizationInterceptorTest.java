package nextstep.subway.unit;

import nextstep.auth.filter.BasicAuthorizationInterceptor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthorizationInterceptorTest extends SpringTest {

    private static final String USERNAME = "test@email.com";
    private static final String PASSWORD = "password";
    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final MockHttpServletResponse response = new MockHttpServletResponse();
    @Autowired
    private BasicAuthorizationInterceptor basicAuthorizationInterceptor;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("test@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @Test
    void authorize() throws Exception {
        // given
        final String token = new String(Base64.getEncoder().encode((USERNAME + ":" + PASSWORD).getBytes()));
        request.addHeader("Authorization", "Basic " + token);

        // when
        final boolean success = basicAuthorizationInterceptor.preHandle(request, response, null);

        // then
        assertThat(success).isTrue();
    }
}
