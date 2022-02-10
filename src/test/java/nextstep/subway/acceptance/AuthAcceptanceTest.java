package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.model.MemberEntitiesHelper.*;

class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        회원가입을_한다();
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청();
        회원_정보_조회됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원가입을_한다();
        String accessToken = 로그인_되어_있음();
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(response);
    }
}
