package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.관리자_로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.나를_삭제하는_요청;
import static nextstep.subway.acceptance.MemberSteps.나의_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.나의_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.유저_로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성됨;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_삭제됨;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_수정됨;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(USER_EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(response);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(USER_EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, USER_EMAIL, AGE);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(USER_EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + USER_EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(USER_EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        회원_정보_삭제됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        var createResponse = 회원_생성_요청(USER_EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        var 회원_정보_조회_응답 = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(회원_정보_조회_응답, USER_EMAIL, AGE);

        var 회원_정보_수정_응답 = 회원_정보_수정_요청(createResponse, "change@mail.com", PASSWORD, AGE);
        회원_정보_수정됨(회원_정보_수정_응답);

        var 회원_삭제_응답 = 회원_삭제_요청(createResponse);
        회원_정보_삭제됨(회원_삭제_응답);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        String accessToken = 유저_로그인_되어_있음();

        var 나의_정보_조회_응답 = 나의_정보_조회_요청(accessToken);
        회원_정보_조회됨(나의_정보_조회_응답, USER_EMAIL, AGE);

        var 나의_정보_수정_응답 = 나의_정보_수정_요청(accessToken, "change@mail.com", PASSWORD, AGE);
        회원_정보_수정됨(나의_정보_수정_응답);

        var 나의_삭제_응답 = 나를_삭제하는_요청(accessToken);
        회원_정보_삭제됨(나의_삭제_응답);
    }
}
