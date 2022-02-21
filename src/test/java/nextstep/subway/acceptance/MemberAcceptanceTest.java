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
     * Scenario : 회원 정보를 관리
     *  When 회원 생성을 요청
     *  Then 회원 생성됨
     *  When 회원 정보 조회 요청
     *  Then 회원 정보 조회됨
     *  When 회원 정보 수정 요청
     *  Then 회원 정보 수정됨
     *  When 회원 삭제 요청
     *  Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);


        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        회원_정보_조회됨(findResponse, EMAIL, AGE);
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}