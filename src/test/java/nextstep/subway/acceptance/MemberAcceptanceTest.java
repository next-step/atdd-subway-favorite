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
     * Feature: 회원 정보를 관리한다.
     *   Scenario: 회원 정보를 관리
     *     When 회원 생성을 요청
     *     Then 회원 생성됨
     *     When 회원 정보 조회 요청
     *     Then 회원 정보 조회됨
     *     When 회원 정보 수정 요청
     *     Then 회원 정보 수정됨
     *     When 회원 삭제 요청
     *     Then 회원 삭제됨
     * */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when then
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 회원_정보_응답 = 회원_정보_조회_요청(회원_생성_응답);
        ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "new" + EMAIL, "new" + PASSWORD, AGE);
        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);

        응답_확인(회원_생성_응답, HttpStatus.CREATED);
        회원_정보_조회됨(회원_정보_응답, EMAIL, AGE);
        응답_확인(회원_수정_응답, HttpStatus.OK);
        응답_확인(회원_삭제_응답, HttpStatus.NO_CONTENT);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}