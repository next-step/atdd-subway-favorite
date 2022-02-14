package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관리 기능")
class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원 생성을 요청합
     * Then 회원 생성됨
     * When 회원 정보 조회 요청
     * Then 회원 정보 조회됨
     * When 회원 정보 수정 요청
     * Then 회원 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // When
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // Then
        회원_생성_확인(createResponse);

        // When
        ExtractableResponse<Response> readResponse = 회원_정보_조회_요청(createResponse);

        // Then
        회원_정보_조회_확인(readResponse);

        // When
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // Then
        회원_정보_수정_확인(updateResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // Then
        회원_정보_삭제_확인(deleteResponse);
    }


    /**
     * Given 회원 생성
     * Given 로그인(JWT) 유지
     * When 나의 정보 조회 요청
     * Then 나의 정보 조회됨
     * When 나의 정보 수정 요청
     * Then 나의 정보 수정됨
     * When 나의 삭제 요청
     * Then 나의 삭제됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // When
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        // Then
        회원_정보_수정_확인(response);

        // When
        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);

        // Then
        회원_정보_수정_확인(updateResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(accessToken);

        // Then
        회원_정보_삭제_확인(deleteResponse);
    }

    private void 회원_정보_삭제_확인(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 회원_정보_수정_확인(ExtractableResponse<Response> updateResponse) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 회원_정보_조회_확인(ExtractableResponse<Response> readResponse) {
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 회원_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}