package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.subway.acceptance.AcceptanceTest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.LoginSteps.깃허브_권한증서_로그인;
import static nextstep.member.LoginSteps.베어러_인증_내_정보_조회;
import static nextstep.member.LoginSteps.베어러_인증_로그인_요청;
import static nextstep.member.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    DataLoader dataLoader;

    @BeforeEach
    void init() {
        dataLoader.loadData();
    }
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given 사용자 1의 깃허브 권한증서를 통해
     * when  로그인을 시도하고 받은 토큰으로 내 정보를 조화하면
     * Then  사용자 1의 이메일을 얻을 수 있다.
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {

        var response = 깃허브_권한증서_로그인(GithubResponse.사용자1.code);
        String token = response.jsonPath().getString("accessToken");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(token).isNotBlank();

        var email = 베어러_인증_내_정보_조회(token).jsonPath().getString("email");
        assertThat(email).isEqualTo(GithubResponse.사용자1.email);
    }

    public enum GithubResponse {
        사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");
        ;

        private final String code;
        private final String accessToken;
        private final String email;

        GithubResponse(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getEmail() {
            return email;
        }
    }

}