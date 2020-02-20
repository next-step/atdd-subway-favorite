package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserLoginResponseView;
import atdd.path.application.dto.UserSighUpResponseView;
import atdd.path.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String KIM_INPUT_JSON = "{\"email\":\"" + KIM_EMAIL + "\",\"password\":\"" + KIM_PASSWORD + "\",\"name\":\"" + KIM_NAME + "\"}";;

    private RestWebClientTest restWebClientTest;

    @BeforeEach
    void setUp() {
        this.restWebClientTest = new RestWebClientTest(this.webTestClient);
    }

    @DisplayName("유저_회원가입이_성공하는지")
    @Test
    public void userSighUp(SoftAssertions softly) {
        //when
        EntityExchangeResult<User> expectResponse
                = restWebClientTest.postMethodAcceptance("/users", KIM_INPUT_JSON, User.class);

        //then
        HttpHeaders responseHeaders = expectResponse.getResponseHeaders();
        User responseBody = expectResponse.getResponseBody();

        //then
        softly.assertThat(responseHeaders.getLocation()).isNotNull();
        softly.assertThat(responseBody.getEmail()).isEqualTo(KIM_EMAIL);
        softly.assertThat(responseBody.getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("유저_회원_탈퇴가_성공하는지")
    @Test
    public void userDelete() {
        //given
        String createLocation = createUser();

        //when
        EntityExchangeResult<Void> expectResponse
                = restWebClientTest.deleteMethodAcceptance(createLocation);

        //then
        assertThat(expectResponse.getStatus()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("유저_회원_로그인이_성공하여_토큰을_리턴하는지")
    @Test
    public void userLogin(SoftAssertions softly) {
        //given
        createUser();

        //when
        EntityExchangeResult<UserLoginResponseView> expectResponse
                = restWebClientTest.postMethodAcceptance("/users/login", LOGIN_USER, UserLoginResponseView.class);

        UserLoginResponseView responseBody = expectResponse.getResponseBody();

        //then
        softly.assertThat(responseBody.getAccessToken()).isNotNull();
        softly.assertThat(responseBody.getTokenType()).isEqualTo("Bearer");

    }



    public String createUser() {
        return Objects.requireNonNull(webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(KIM_INPUT_JSON), String.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(UserSighUpResponseView.class)
                .getResponseHeaders()
                .getLocation())
                .getPath();
    }
}
