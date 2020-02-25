package atdd.member.web;

import static atdd.member.MemberConstant.LOGIN_MEMBER_VIEW;
import static atdd.member.MemberConstant.MEMBER_BASE_URL;
import static atdd.member.MemberConstant.MEMBER_VIEW;
import static org.assertj.core.api.Assertions.assertThat;

import atdd.member.application.dto.MemberResponseView;
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
public class MemberAcceptanceTest {


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
    @DisplayName("회원 가입")
    public void join() {
        // when
        EntityExchangeResult<MemberResponseView> response = memberHttpTest.join(MEMBER_VIEW);
        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getEmail()).isNotNull().isEqualTo(MEMBER_VIEW.getEmail());
        assertThat(response.getResponseBody().getName()).isNotNull().isEqualTo(MEMBER_VIEW.getName());
    }


    @Test
    @DisplayName("회원 탈퇴")
    public void createStation() {
        //given
        EntityExchangeResult<MemberResponseView> response = memberHttpTest.join(MEMBER_VIEW);
        // when & than
        webTestClient.delete().uri(MEMBER_BASE_URL + "/" + response.getResponseBody().getId())
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("회원 조회")
    public void me() {
        //given & when
        memberHttpTest.join(MEMBER_VIEW);
        EntityExchangeResult<MemberResponseView> response = memberHttpTest
            .me(loginHttpTest.getToken(LOGIN_MEMBER_VIEW));
        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getEmail()).isNotNull().isEqualTo(MEMBER_VIEW.getEmail());
        assertThat(response.getResponseBody().getName()).isNotNull().isEqualTo(MEMBER_VIEW.getName());
    }

}