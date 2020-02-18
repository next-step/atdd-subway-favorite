package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.MemberResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AbstractAcceptanceTest
{
    public static final String MEMBER_URL = "/members";

    private MemberHttpTest memberHttpTest;
    private LoginHttpTest loginHttpTest;

    @BeforeEach
    void setUp()
    {
        this.memberHttpTest = new MemberHttpTest(webTestClient);
        this.loginHttpTest = new LoginHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    public void createMember()
    {
        // when
        Long memberId = memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

        // then
        EntityExchangeResult<MemberResponseView> response = memberHttpTest.retrieveMember(memberId);
        assertThat(response.getResponseBody().getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(response.getResponseBody().getName()).isEqualTo(MEMBER_NAME);
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void deleteMember()
    {
        // given
        Long memberId = memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
        EntityExchangeResult<MemberResponseView> response = memberHttpTest.retrieveMember(MEMBER_ID);

        // when
        webTestClient.delete().uri(MEMBER_URL + "/" + memberId)
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri(MEMBER_URL + "/" + memberId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("회원 정보 요청")
    @Test
    public void retrieveMyInfo()
    {
        // given
        Long memberId = memberHttpTest.createMember(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

        // and
        String accessToken = loginHttpTest.login(MEMBER_EMAIL, MEMBER_PASSWORD).getResponseBody().getAccessToken();

        // when
        EntityExchangeResult<MemberResponseView> response
                = memberHttpTest.retrieveMyInfo(MEMBER_EMAIL, MEMBER_PASSWORD, accessToken);

        // then
        assertThat(response.getResponseBody().getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(response.getResponseBody().getName()).isEqualTo(MEMBER_NAME);
    }
}
