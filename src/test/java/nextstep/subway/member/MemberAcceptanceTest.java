package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;
    public static final MemberRequest 신규회원 = new MemberRequest(EMAIL, PASSWORD, AGE);

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(신규회원);

        // then
        회원_생성됨(response);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(신규회원);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(신규회원);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(신규회원);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        //when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(신규회원);
        //then
        회원_생성됨(createResponse);

        //when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);
        //then
        회원_정보_조회됨(response, EMAIL, AGE);

        //when
        response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE + 1);
        //then
        회원_정보_수정됨(response);
        수정된_회원_정보_확인(createResponse, NEW_EMAIL, NEW_AGE);

        //when
        response = 회원_삭제_요청(createResponse);
        //then
        회원_삭제됨(response);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(신규회원);
        회원_생성됨(createResponse);

        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);
        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        response = 내_정보_수정_요청(tokenResponse, new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        // then
        회원_정보_수정됨(response);
        수정된_회원_정보_확인(createResponse, NEW_EMAIL, NEW_AGE);

        // when
        response = 내_정보_삭제_요청(tokenResponse);
        // then
        회원_삭제됨(response);
    }
}