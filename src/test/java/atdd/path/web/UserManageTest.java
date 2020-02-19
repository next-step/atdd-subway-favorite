package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class UserManageTest extends AbstractAcceptanceTest {
//    Feature: 회원 정보 관리
//
//    Scenario: 회원 가입
//    Given -
//    When 사용자는 자신의 정보 등록을 요청한다.
//    Then 회원 등록이 되었다.
//
//    Scenario: 회원 탈퇴
//    Given 사용자는 회원가입을 했다.
//    When 사용자는 탈퇴 요청한다.
//    Then 회원 탈퇴가 되었다.
//
    @BeforeEach
    void setUp() {}

    @DisplayName("회원 가입")
    @Test
    public EntityExchangeResult<UserResponseView> createUser() {
        String email = "boorwonie@email.com";
        String name = "브라운";
        String password = "subway";
        String inputJson = "{\"email\":\"" + email + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"password\":\"" + password + "\"}";

        // when
        EntityExchangeResult<UserResponseView> userResponse = webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class)
                .returnResult();

        // then
        assertThat(userResponse.getResponseBody().getEmail()).isEqualTo(email);
        return userResponse;
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void dreateUser() {
        EntityExchangeResult<UserResponseView> userResponse = createUser();

        // when
        webTestClient.delete().uri("/user" + "/" + userResponse.getResponseBody().getId())
                .exchange()
                .expectStatus().isNoContent();

        // then
        webTestClient.get().uri("/user" + "/" + userResponse.getResponseBody().getId())
                .exchange()
                .expectStatus().isNotFound();

    }
}
