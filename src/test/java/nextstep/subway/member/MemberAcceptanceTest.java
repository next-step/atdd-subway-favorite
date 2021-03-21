package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.member.MemberSteps.내_회원_정보_수정됨;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(response);
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

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when & then
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        // when & then
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when & then
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(updateResponse);

        // when & then
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("내 회원 정보를 조회한다.")
    @Test
    void getMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        // then
        내_회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("내 회원 정보를 수정한다.")
    @Test
    void updateMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("email", "new" + EMAIL);
        params.put("password", "new" + PASSWORD);
        params.put("age", String.valueOf(AGE));

        ExtractableResponse<Response> response = 내_회원_정보_수정_요청(tokenResponse, params);

        // then
        내_회원_정보_수정됨(response);
    }

    @DisplayName("내 회원 정보를 삭제한다.")
    @Test
    void deleteMemberOfMine() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_삭제_요청(tokenResponse);

        // then
        내_회원_삭제됨(response);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // when & then
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when & then
        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(tokenResponse);
        내_회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when & then
        Map<String, String> params = new HashMap<>();
        params.put("email", "new" + EMAIL);
        params.put("password", "new" + PASSWORD);
        params.put("age", String.valueOf(AGE));

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(tokenResponse, params);
        내_회원_정보_수정됨(updateResponse);

        // when & then
        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(tokenResponse);
        내_회원_삭제됨(deleteResponse);
    }
}