package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String USER_URI = "/users";
    public static final String SIGN_IN_URI = USER_URI + "/login";
    public static final String FIRST_USER_INPUT_JSON = String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\"}",
            "boorwonie@email.com", "브라운", "subway");
    public static final String SECOND_USER_INPUT_JSON = String.format("{\"email\": \"%s\", \"name\": \"%s\", \"password\": \"%s\"}",
            "irrationnelle@email.com", "라세", "station");
    public static final String SIGN_IN_JSON = String.format("{\"email\": \"%s\", \"password\" : \"%s\"}",
            "boorwonie@email.com", "subway");

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    public void createUser() {
        // when
        userHttpTest.createUserSuccess(USER_URI, FIRST_USER_INPUT_JSON);

        // then
        EntityExchangeResult<List<UserResponseView>> userResponseAfterGet = userHttpTest
                .showUsersAndSingleUserTest(USER_URI)
                .expectBodyList(UserResponseView.class)
                .returnResult();

        assertThat(userResponseAfterGet.getResponseBody().get(0).getName()).isEqualTo("브라운");
    }

    @DisplayName("회원 탈퇴")
    @Test
    public void deleteUser() {
        // given
        Long firstUserId = userHttpTest.createUserSuccess(USER_URI, FIRST_USER_INPUT_JSON);
        Long secondUserId = userHttpTest.createUserSuccess(USER_URI, SECOND_USER_INPUT_JSON);

        String reqUriForFirstUser = USER_URI + "/" + firstUserId;

        // when
        webTestClient.delete()
                     .uri(reqUriForFirstUser)
                     .exchange()
                     .expectStatus().isNoContent();

        // then
        webTestClient.get()
                     .uri(reqUriForFirstUser)
                     .exchange()
                     .expectStatus().isNotFound();

        String reqUriForSecondUser = USER_URI + "/" + secondUserId;
        EntityExchangeResult<UserResponseView> userResponseAfterGet =
                userHttpTest.showUsersAndSingleUserTest(reqUriForSecondUser)
                            .expectBody(UserResponseView.class)
                            .returnResult();

        assertThat(userResponseAfterGet.getResponseBody().getName()).isEqualTo("라세");
    }

    @DisplayName("로그인 요청 후 토큰 발급")
    @Test
    public void signInUser() {
        // given
        userHttpTest.createUserSuccess(USER_URI, FIRST_USER_INPUT_JSON);

        // when
        String tokenInfo = userHttpTest.createAuthorizationTokenSuccess(SIGN_IN_URI, SIGN_IN_JSON);

        // then
        assertThat(tokenInfo).isNotEmpty();

        // when
        String improperJson = String.format("{\"email\": \"%s\", \"password\" : \"%s\"}", "boorwonie@email.com", "station");

        // then
        userHttpTest.createAuthorizationTokenTest(SIGN_IN_URI, improperJson).isUnauthorized();
    }

    @DisplayName("발급받은 토큰으로 유저 정보 받아오기")
    @Test
    public void getUserInfoWithToken() {
        // given
        userHttpTest.createUserSuccess(USER_URI, FIRST_USER_INPUT_JSON);
        String tokenInfo = userHttpTest.createAuthorizationTokenSuccess(SIGN_IN_URI, SIGN_IN_JSON);

        // when and then
        webTestClient.get()
                     .uri(USER_URI + "/me")
                     .header("Authorization", tokenInfo)
                     .exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(MediaType.APPLICATION_JSON)
                     .expectBody(UserResponseView.class).consumeWith(this::getUserInfoAssert);
    }

    private void getUserInfoAssert(EntityExchangeResult<UserResponseView> result) {
        assertThat(result.getResponseBody().getName()).isEqualTo("브라운");
        assertThat(result.getResponseBody().getEmail()).isEqualTo("boorwonie@email.com");
    }
}
