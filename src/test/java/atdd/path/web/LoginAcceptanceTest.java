package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.UserRepository;
import atdd.path.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class LoginAcceptanceTest extends AbstractAcceptanceTest {
    public static final String LOGIN_BASE_URI = "/login";
    public static final String NAME = "브라운";
    public static final String EMAIL = "boorwonie@email.com";
    public static final String PASSWORD = "subway";
    private UserHttpTest userHttpTest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void 로그인_요청하기() {
        //given
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);

        //when
        LoginRequestView requestView = new LoginRequestView(EMAIL, PASSWORD);

        //then
        webTestClient.post().uri(LOGIN_BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestView), LoginRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.accessToken").isNotEmpty()
                .jsonPath("$.tokenType").isNotEmpty();
    }

    @Test
    public void 회원정보_요청하기() {
        //given
        userHttpTest.createUser(EMAIL, NAME, PASSWORD);
        String token = jwtTokenProvider.createToken(EMAIL);

        //when
        webTestClient.get().uri(LOGIN_BASE_URI + "/me")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);
    }
}
