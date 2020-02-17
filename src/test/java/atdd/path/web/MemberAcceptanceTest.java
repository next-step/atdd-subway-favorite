package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.MemberResponseView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.Map;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("회원 가입을 할 수 있다")
    @Test
    void beAbleToJoin() throws Exception {
        EntityExchangeResult<MemberResponseView> result = createMember();
        MemberResponseView view = result.getResponseBody();

        assertThat(view).isNotNull();
        assertThat(view.getEmail()).isEqualTo(TEST_MEMBER_EMAIL);
        assertThat(view.getName()).isEqualTo(TEST_MEMBER_NAME);
        assertThat(view.getPassword()).isEqualTo(TEST_MEMBER_PASSWORD);
    }

    @DisplayName("회원 탈퇴를 할 수 있다")
    @Test
    void beAbleToWithdrawal() throws Exception {
        EntityExchangeResult<MemberResponseView> result = createMember();
        MemberResponseView view = result.getResponseBody();

        webTestClient.delete().uri("/members/" + view.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void beAbleToLogin() throws Exception {
        createMember();

        Map<String, String> map = Map.ofEntries(
                Map.entry("email", TEST_MEMBER_EMAIL),
                Map.entry("password", TEST_MEMBER_PASSWORD)
        );

        String inputJson = objectMapper.writeValueAsString(map);

        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.tokenType").isEqualTo("bearer");

    }

    private EntityExchangeResult<MemberResponseView> createMember() throws Exception {
        String inputJson = objectMapper.writeValueAsString(TEST_MEMBER);

        return webTestClient.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(MemberResponseView.class)
                .returnResult();
    }

}
