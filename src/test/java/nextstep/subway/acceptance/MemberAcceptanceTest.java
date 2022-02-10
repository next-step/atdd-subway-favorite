package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.model.MemberEntitiesHelper.내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.내_회원_정보를_삭제_한다;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.내_회원_정보를_수정_한다;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.로그인_되어_있음;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.회원_정보_조회됨;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.회원_정보를_삭제_한다;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.회원_정보를_수정_한다;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.회원_정보를_조회한다;
import static nextstep.subway.acceptance.model.MemberEntitiesHelper.회원가입을_한다;

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
        회원가입을_한다();
        String accessToken = 로그인_되어_있음();
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(response);
        내_회원_정보를_수정_한다(accessToken);
        내_회원_정보를_삭제_한다(accessToken);
    }
}