package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthAcceptanceTest.깃허브_인증_로그인_요청_파라미터_생성;
import static nextstep.subway.acceptance.AuthSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.AuthSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("jwt 토큰으로 내 정보를 조회한다.")
    @Test
    void getMyInfoByJwt() {
        ExtractableResponse<Response> loginResponse = 베어러_인증_로그인_요청(DataLoader.EMAIL, DataLoader.PASSWORD);

        ExtractableResponse<Response> response = 토큰으로_회원_정보_조회_요청(loginResponse);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo(DataLoader.EMAIL);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(DataLoader.AGE);
    }

    @DisplayName("깃허브 토큰으로 내 정보를 조회한다.")
    @Test
    void getMyInfoByGithub() {
        ExtractableResponse<Response> loginResponse = 깃허브_인증_로그인_요청(깃허브_인증_로그인_요청_파라미터_생성());

        ExtractableResponse<Response> response = 토큰으로_회원_정보_조회_요청(loginResponse);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo(DataLoader.EMAIL);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(DataLoader.AGE);
    }
}