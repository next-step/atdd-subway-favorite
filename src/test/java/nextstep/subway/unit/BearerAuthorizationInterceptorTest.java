package nextstep.subway.unit;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.filter.BearerAuthorizationInterceptor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BearerAuthorizationInterceptorTest extends SpringTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final MockHttpServletResponse response = new MockHttpServletResponse();
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private BearerAuthorizationInterceptor bearerAuthorizationInterceptor;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("test@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    @DisplayName("베어러 토큰 인증")
    @Test
    void authorize() throws Exception {
        // given
        final String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        request.addHeader("Authorization", "Bearer " + token);

        // when
        bearerAuthorizationInterceptor.preHandle(request, response, null);

        // then
        assertThat(request.getAttribute("user")).isNotNull();
    }
}
