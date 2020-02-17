package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.MemberResponseView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Supplier;

import static atdd.path.TestConstant.*;
import static atdd.path.TestUtils.jsonOf;
import static atdd.path.application.provider.JwtTokenProvider.AUTHORIZATION;
import static atdd.path.application.provider.JwtTokenProvider.TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("회원 가입을 할 수 있다")
    @Test
    void beAbleToJoin() {
        MemberResponseView view = getResponseView(this::createMember);

        assertThat(view).isNotNull();
        assertThat(view.getEmail()).isEqualTo(TEST_MEMBER_EMAIL);
        assertThat(view.getName()).isEqualTo(TEST_MEMBER_NAME);
        assertThat(view.getPassword()).isEqualTo(TEST_MEMBER_PASSWORD);
    }

    @DisplayName("회원 탈퇴를 할 수 있다")
    @Test
    void beAbleToWithdrawal() {
        MemberResponseView view = getResponseView(this::createMember);

        webTestClient.delete().uri("/members/" + view.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void beAbleToLogin() {
        createMember();

        LoginResponseView view = getResponseView(this::loginMember);

        assertThat(view).isNotNull();
        assertThat(view.getTokenType()).isEqualTo(TOKEN_TYPE);
        assertThat(view.getAccessToken()).isNotEmpty();
    }

    @DisplayName("회원 자신의 정보를 조회 할 수 있다")
    @Test
    void beAbleToFindMe() {
        createMember();

        LoginResponseView view = getResponseView(this::loginMember);
        String authorization = view.getTokenType() +" "+ view.getAccessToken();

        webTestClient.get().uri("/members/me")
                .header(AUTHORIZATION, authorization)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_MEMBER_EMAIL)
                .jsonPath("$.name").isEqualTo(TEST_MEMBER_NAME);

    }

    private EntityExchangeResult<MemberResponseView> createMember() {
        String inputJson = jsonOf(TEST_MEMBER);

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

    private EntityExchangeResult<LoginResponseView> loginMember() {
        Map<String, Object> map = Map.ofEntries(
                Map.entry("email", TEST_MEMBER_EMAIL),
                Map.entry("password", TEST_MEMBER_PASSWORD)
        );

        String inputJson = jsonOf(map);

        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult();
    }

    private <T> T getResponseView(Supplier<EntityExchangeResult<T>> supplier) {
        EntityExchangeResult<T> result = supplier.get();
        return result.getResponseBody();
    }

}
