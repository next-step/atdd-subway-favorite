package atdd.week2.controller;

import atdd.AbstractAcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Feature: 회원 정보 관리
     *
     * Scenario: 회원 가입
     * Given -
     * When 사용자는 자신의 정보 등록을 요청한다.
     * Then 회원 등록이 되었다.
     *
     * Scenario: 회원 탈퇴
     * Given 사용자는 회원가입을 했다.
     * When 사용자는 탈퇴 요청한다.
     * Then 회원 탈퇴가 되었다.
     */

    /**
     * 회원 정보
     * {
     *     "email":boorwonie@email.com,
     *     "name":"브라운",
     *     "password":"subway"
     * }
     */

    @Test
    void createUser() {

        String inputJson = "{\"email\":\"boorwonie@email.com\"" +"," +"\"name\":\"브라운\"" +"," +"\"password\":\"subway\"}";

        System.out.println(inputJson);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location");

    }

}
