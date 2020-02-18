package atdd.member.web;

import static org.assertj.core.api.Assertions.assertThat;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class MemberAcceptanceTest {

    private static final String MEMBER_BASE_URL = "members";
    private static final String MEMBER_EMAIL = "seok2@naver.com";
    private static final String MEMBER_NAME = "이재석";
    private static final CreateMemberRequestView MEMBER = CreateMemberRequestView.of(MEMBER_EMAIL, MEMBER_NAME, "1234");

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("회원 가입")
    public void join() {
        // when
        EntityExchangeResult<MemberResponseView> response = join(MEMBER);

        // then
        assertThat(response.getResponseBody().getId()).isNotNull();
        assertThat(response.getResponseBody().getEmail()).isNotNull().isEqualTo(MEMBER_EMAIL);
        assertThat(response.getResponseBody().getName()).isNotNull().isEqualTo(MEMBER_NAME);
    }


    @Test
    @DisplayName("회원 탈퇴")
    public void createStation() {

        //given
        EntityExchangeResult<MemberResponseView> response = join(MEMBER);

        // when & than
        webTestClient.delete().uri(MEMBER_BASE_URL + "/" + response.getResponseBody().getId())
            .exchange()
            .expectStatus().isNoContent();

    }

    private EntityExchangeResult<MemberResponseView> join(CreateMemberRequestView view) {
        return webTestClient.post().uri(MEMBER_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(view), CreateMemberRequestView.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(MemberResponseView.class)
            .returnResult();
    }

}