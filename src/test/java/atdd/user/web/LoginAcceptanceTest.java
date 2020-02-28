package atdd.user.web;

import atdd.Constant;
import atdd.AbstractAcceptanceTest;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.UserRepository;
import atdd.user.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.Constant.AUTH_SCHEME_BEARER;

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
        LoginRequestView requestView = new LoginRequestView(EMAIL, PASSWORD, jwtTokenProvider);

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

        //when, then
        webTestClient.get().uri(Constant.USER_BASE_URI + "/me")
                .header("Authorization", AUTH_SCHEME_BEARER + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(EMAIL)
                .jsonPath("$.password").isEqualTo(PASSWORD);
    }
}