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

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when 회원 생성을 요청
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then 회원 생성됨
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 회원 정보 조회 요청
        ExtractableResponse<Response> getResponse = 회원_정보_조회_요청(createResponse);
        // then 회원 정보 조회됨
        회원_정보_조회됨(getResponse, EMAIL, AGE);

        // when 회원 정보 수정 요청
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then 회원 정보 수정됨
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when 회원 삭제 요청
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then 회원 삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> getResponse = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(getResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(accessToken);
        회원_정보_삭제됨(deleteResponse);
    }

}