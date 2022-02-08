package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW = "new";
    public static final int AGE = 20;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updatedResponse = 회원_정보_수정_요청(createResponse, NEW + EMAIL, NEW + PASSWORD, AGE);

        // then
        회원_정보_수정됨(updatedResponse);

        // when
        ExtractableResponse<Response> deletedResponse = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(deletedResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updatedResponse = 내_회원_정보_수정_요청(accessToken, NEW + EMAIL, NEW + PASSWORD, AGE);

        // then
        회원_정보_수정됨(updatedResponse);

        // when
        ExtractableResponse<Response> deletedResponse = 내_회원_정보_삭제_요청(accessToken);

        // then
        회원_삭제됨(deletedResponse);
    }
}
