package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.로그인_회원_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> result = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(result, "new" + EMAIL, AGE);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원생성 요청
     * When 회원 정보요청
     * Then 회원 조회 됨
     * When 회원 정보 수정
     * Then 회원 정보 수정 됨
     * When 회원 정보 삭제
     * Then 회원 정보 삭제 됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // given
        ExtractableResponse<Response> 회원생성 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> 회원정보조회 = 회원_정보_조회_요청(회원생성);

        // then
        회원_정보_조회됨(회원정보조회, EMAIL, AGE);

        // when
        회원_정보_수정_요청(회원생성, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        ExtractableResponse<Response> 회원수정정보조회 = 회원_정보_조회_요청(회원생성);
        회원_정보_조회됨(회원수정정보조회, "new" + EMAIL, AGE);

        // when
        ExtractableResponse<Response> 회원삭제 = 회원_삭제_요청(회원생성);

        // then
        assertThat(회원삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원생성 요청
     * When 로그인 후 로그인 회원 정보 조회
     * Then 로그인 회원 조회 됨
     * When 로그인 회원 정보 수정
     * Then 로그인 정보 수정 됨
     * When 로그인 정보 삭제
     * Then 로그인 정보 삭제 됨
     */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        String 로그인토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> 로그인회원조회 = 로그인_회원_정보_조회_요청(로그인토큰);

        // then
        회원_정보_조회됨(로그인회원조회, EMAIL, AGE);

        // when
        String newEmail = "new" + EMAIL;
        String newEassword = "new" + PASSWORD;

        ExtractableResponse<Response> 로그인회원정보수정 = 로그인_회원_정보_수정_요청(로그인토큰, newEmail, newEassword, AGE);
        String 재로그인토큰 = 로그인_되어_있음(newEmail, newEassword);

        // then
        ExtractableResponse<Response> 로그인회원수정조회 = 로그인_회원_정보_조회_요청(재로그인토큰);
        회원_정보_조회됨(로그인회원수정조회, newEmail, AGE);

        // when
        ExtractableResponse<Response> 로그인회원삭제 = 로그인_회원_삭제_요청(재로그인토큰);

        // then
        assertThat(로그인회원삭제.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}