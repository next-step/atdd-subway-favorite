package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.UserResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {

    public static final String USER_URL = "/users";

    public UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("회원 가입")
    @Test
    void createUserTest() {
        // when
        UserResponseView response = userHttpTest.createUserTest(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();

        // then
        assertThat(response.getName()).isEqualTo(TestConstant.NAME_BROWN);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUserTest() {
        // given
        UserResponseView response = userHttpTest.createUserTest(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();

        // when
        webTestClient.delete().uri(USER_URL + "/" + response.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @DisplayName("회원 정보 조회")
    @Test
    void getUserInfoTest() {
        // given
        userHttpTest.createUserTest(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);
        LoginResponseView responseView = userHttpTest
                .loginUserTest(TestConstant.EMAIL_BROWN, TestConstant.PASSWORD_BROWN).getResponseBody();

        webTestClient.post().uri(USER_URL + "/me")
                .header("Authorization",
                        String.format("%s %s", responseView.getTokenType(), responseView.getAccessToken()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.email").isEqualTo(TestConstant.EMAIL_BROWN);
    }
}
