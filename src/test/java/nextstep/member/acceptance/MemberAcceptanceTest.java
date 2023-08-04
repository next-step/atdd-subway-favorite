package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.study.AuthSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 한다.
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given : 선행조건 기술
        var 회원생성응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 로그인_응답 = AuthSteps.로그인요청(EMAIL, PASSWORD);
        String accessToken = getAccessToken(로그인_응답);

        // when : 기능 수행
        var 회원_정보_조회_응답 = 액세스토큰으로_회원_정보_조회_요청(accessToken);

        // then : 결과 확인
        assertThat(회원생성응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        액세스_토큰으로_회원_정보_조회됨(회원_정보_조회_응답);
    }

    private void 액세스_토큰으로_회원_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.body().jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.body().jsonPath().getInt("age")).isEqualTo(AGE);
    }
}