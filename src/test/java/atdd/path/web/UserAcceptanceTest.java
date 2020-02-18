package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String USER_URL = "/users";
    String firstInputJson = String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\"}", "boorwonie" +
            "@email.com", "브라운", "subway");
    String secondInputJson = String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\"}",
            "irrationnelle@email.com", "라세", "station");

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    public void createUser() {
        // when
        userHttpTest.createUserSuccess(USER_URL, firstInputJson);

        // then
        EntityExchangeResult<List<UserResponseView>> userResponseAfterGet = userHttpTest
                .showUsersAndSingleUserTest(USER_URL)
                .expectBodyList(UserResponseView.class)
                .returnResult();

        assertThat(userResponseAfterGet.getResponseBody().get(0).getName()).isEqualTo("브라운");
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void deleteUser() {
        // given
        Long firstUserId = userHttpTest.createUserSuccess(USER_URL, firstInputJson);
        Long secondUserId = userHttpTest.createUserSuccess(USER_URL, secondInputJson);

        // when
        webTestClient.delete().uri(USER_URL + "/" + firstUserId).exchange().expectStatus().isNoContent();

        // then
        webTestClient.get().uri(USER_URL + "/" + firstUserId).exchange().expectStatus().isNotFound();

        String reqUriForSingleUser = USER_URL + "/" + secondUserId;
        EntityExchangeResult<UserResponseView> userResponseAfterGet = userHttpTest
                .showUsersAndSingleUserTest(reqUriForSingleUser)
                .expectBody(UserResponseView.class)
                .returnResult();

        assertThat(userResponseAfterGet.getResponseBody().getName()).isEqualTo("라세");
    }

    @DisplayName("로그인 요청 후 토큰 발급")
    @Test
    public void signInUser() {
        // given
        Long firstUserId = userHttpTest.createUserSuccess(USER_URL, firstInputJson);

        String signInUri = "/login";
        String inputJson = String.format("{\"email\": %s, \"password\" : %s", "boorwonie@email.com", "subway");

        EntityExchangeResult<?> response = webTestClient
                .post()
                .uri(signInUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.tokenType").isNotEmpty()
                .returnResult();
    }
}
