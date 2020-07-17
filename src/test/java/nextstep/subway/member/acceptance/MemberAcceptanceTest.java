package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // 회원 가입을 테스트
        ExtractableResponse<Response> createResponse = 회원_가입_테스트();

        // 가입된 회원 조회 테스트
        회원_조회_테스트(createResponse);

        // 가입된 회원 정보 수정 테스
        회원_정보_수정_테스트(createResponse);

        // 가입된 회원정보삭제 테스트
        회원_정보_삭제_테스트(createResponse);
    }

    private ExtractableResponse<Response> 회원_가입_테스트() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        회원_생성됨(createResponse);
        return createResponse;
    }

    private void 회원_조회_테스트(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    private void 회원_정보_수정_테스트(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> deleteResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(deleteResponse);
    }

    private void 회원_정보_삭제_테스트(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(deleteResponse);
    }
}
