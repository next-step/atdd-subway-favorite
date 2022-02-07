package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_응답, EMAIL, AGE);

        ExtractableResponse<Response> 회원_정보_응답 = 회원_정보_조회_요청(회원_생성_응답);
        회원_정보_조회됨(회원_정보_응답, EMAIL, AGE);

        Long userId = 회원_정보_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "new" + EMAIL, "new" + PASSWORD, AGE);
        회원_정보_수정됨(회원_수정_응답, userId, "new" + EMAIL, AGE);

        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);
        회원_정보_삭제됨(회원_삭제_응답, userId);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_응답, EMAIL, AGE);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> 내_회원_정보_응답 = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(내_회원_정보_응답, EMAIL, AGE);

        Long userId = 내_회원_정보_응답.jsonPath().getLong("id");

        ExtractableResponse<Response> 내_회원_수정_응답 = 내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);
        회원_정보_수정됨(내_회원_수정_응답, userId, "new" + EMAIL, AGE);

        ExtractableResponse<Response> 내_회원_삭제_응답 = 내_회원_정보_삭제_요청(accessToken);
        회원_정보_삭제됨(내_회원_삭제_응답, userId);
    }
}