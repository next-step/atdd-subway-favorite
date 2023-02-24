package nextstep.login.unit;

import nextstep.login.ui.AuthenticationService;
import nextstep.login.ui.LoginResponse;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("로그인 인증 기능")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인 테스트")
    void test() {
        memberService.createMember(new MemberRequest("dev.gibeom@gmail.com", "서비스로가는길", 27));

        LoginResponse login = authenticationService.login("dev.gibeom@gmail.com", "서비스로가는길");

        assertThat(login.getAccessToken().split("\\.")).hasSize(3);
    }
}
