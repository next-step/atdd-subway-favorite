package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 정보를 관리한다.")
class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원 생성을 요청
     * Then 회원 생성됨
     * When 회원 정보 조회 요청
     * Then 회원 정보 조회됨
     * When 회원 정보 수정 요청
     * Then 회원 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원 생성 요청
     * When 나의 정보 조회 요청
     * Then 나의 정보 조회됨
     * When 나의 정보 수정 요청
     * Then 나의 정보 수정됨
     * When 나의 정보 삭제 요청
     * Then 나의 정보 삭제됨
     */
    @DisplayName("나의 회원 정보를 토큰으로 조회, 수정, 삭제한다.")
    @Test
    void manageMyInfoSession() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        // when
        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(accessToken);
        // then
        내_회원_정보_조회됨(findResponse, EMAIL, AGE);

        // given
        String newEmail = "new" + EMAIL;
        String newPassword = "new" + PASSWORD;
        // when
        ExtractableResponse<Response> editResponse = 내_회원_정보_수정_요청(accessToken, newEmail, newPassword, AGE);
        // then
        내_회원_정보_수정됨(editResponse, newEmail);

        // when
        ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(accessToken);
        // then
        내_회원_정보_삭제됨(deleteResponse);
    }
}