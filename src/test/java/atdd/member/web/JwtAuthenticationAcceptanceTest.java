package atdd.member.web;


import static atdd.member.MemberConstant.MEMBER_VIEW;
import static org.assertj.core.api.Assertions.assertThat;

import atdd.member.application.dto.LoginMemberRequestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class JwtAuthenticationAcceptanceTest {

    @Autowired
    private WebTestClient webTestClient;
    private MemberHttpTest memberHttpTest;
    private LoginHttpTest loginHttpTest;

    @BeforeEach
    void setUp() {
        this.memberHttpTest = new MemberHttpTest(webTestClient);
        this.loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @Test
    @DisplayName("로그인")
    public void join() {
        // when
        memberHttpTest.join(MEMBER_VIEW);
//        EntityExchangeResult<String> response = loginHttpTest.login(LOGIN_MEMBER_VIEW);
        EntityExchangeResult<String> response = loginHttpTest.login(new LoginMemberRequestView("soek3@kakao.com", "!"));
        // then
        assertThat(response.getResponseBody()).isNotNull();
    }

}
