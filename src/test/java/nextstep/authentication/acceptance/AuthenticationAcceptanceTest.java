package nextstep.authentication.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.UserInformation.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관련 인수 테스트")
public class AuthenticationAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this);
        memberRepository.save(new Member(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge()));
    }

    /**
     * Given 이메일과 비밀번호를 입력하고,
     * When 로그인을 요청하면,
     * Then 접근 토큰이 발급된다.
     */
    @DisplayName("이메일과 비밀번호를 입력하고 요청하면 접근토큰이 발급된다.")
    @Test
    void bearerAuthentication() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", 사용자1.getEmail());
        params.put("password", 사용자1.getPassword());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        assertThat(getAccessToken(response)).isNotBlank();
    }

    /**
     * Given Github에서 발급받은 코드를 입력하고,
     * When 로그인을 요청하면,
     * Then 접근 토큰이 발급된다.
     */
    @DisplayName("Github에서 발급받은 코드를 입력하고 로그인 요청하면 접근토큰이 발급된다.")
    @Test
    void githubAuthentication() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자1.getCode());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        // then
        assertThat(getAccessToken(response)).isNotBlank();
    }

    private static String getAccessToken(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getString("accessToken");
    }
}
