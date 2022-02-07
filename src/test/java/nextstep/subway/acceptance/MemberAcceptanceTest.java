package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.model.MemberEntitiesHelper.*;

@DisplayName("회원 관리 기능")
class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 회원_정보를_관리한다() {
        ExtractableResponse<Response> response = 회원가입을_한다();
        회원_정보를_조회한다(response);
        회원_정보를_수정_한다(response);
        회원_정보를_삭제_한다(response);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}