package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.LineResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

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
    public void CreateUser() {
        String email = "boorwonie@email.com";
        String name = "브라운";
        String password = "subway";
        String inputJson = "{\"email\":\"" + email + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"password\":\"" + password + "\"}";

        webTestClient.post().uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(inputJson), String.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectHeader().exists("Location")
            .expectBody(LineResponseView.class);
    }
}
