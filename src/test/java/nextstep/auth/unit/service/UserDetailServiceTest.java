package nextstep.auth.unit.service;

import nextstep.auth.authentication.UserDetails;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.application.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.EMAIL;
import static nextstep.auth.unit.model.AuthenticationUnitTestHelper.getMemberRequest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserDetailServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
        memberService.createMember(getMemberRequest());
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        assertThat(userDetails.getEmail()).isEqualTo(EMAIL);
    }
}
