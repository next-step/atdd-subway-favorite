package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.TestConstant;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {

    public static final String LOGIN_URL = "/login";

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("로그인")
    @Test
    void login() {
        //given
        userHttpTest.createUser(TestConstant.EMAIL_BROWN, TestConstant.NAME_BROWN, TestConstant.PASSWORD_BROWN);

        // when, then
        LoginRequestView request = LoginRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();

        webTestClient.post().uri(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponseView.class)
                .returnResult().getResponseBody()
                .getTokenType().contentEquals("Bearer");
    }
}
