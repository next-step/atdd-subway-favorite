package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // When
        ExtractableResponse<Response> readResponse = 회원_정보_조회_요청(createResponse);

        // Then
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // When
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // Then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // When
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // Then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}