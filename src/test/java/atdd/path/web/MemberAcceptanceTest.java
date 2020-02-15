package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.path.TestConstant.*;

public class MemberAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("회원 가입을 할 수 있다")
    @Test
    void beAbleToJoin() throws Exception {
        String inputJson = objectMapper.writeValueAsString(TEST_MEMBER);

        webTestClient.post().uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.email").isEqualTo(TEST_MEMBER_EMAIL)
                .jsonPath("$.name").isEqualTo(TEST_MEMBER_NAME)
                .jsonPath("$.password").isEqualTo(TEST_MEMBER_PASSWORD);
    }

}
