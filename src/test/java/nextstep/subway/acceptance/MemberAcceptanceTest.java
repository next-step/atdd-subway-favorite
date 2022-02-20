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
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_정보_생성됨(createResponse);


        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(response, EMAIL, AGE);


        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        // then
        회원_정보_수정됨(updateResponse);


        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_정보_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(response, EMAIL, AGE);

        final String editEmail = "edit@editEmail";
        final String editPassword = "editPassword";
        final Integer editAge = 30;

        final ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(accessToken, editEmail, editPassword, editAge);
        회원_정보_수정됨(updateResponse);
        회원_정보_조회됨(updateResponse, editEmail, editAge);

        final ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(accessToken);
        회원_정보_삭제됨(deleteResponse);
    }
}