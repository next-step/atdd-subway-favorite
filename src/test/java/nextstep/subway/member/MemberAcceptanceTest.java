package nextstep.subway.member;

import com.sun.org.apache.bcel.internal.generic.NEW;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
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
        // 회원 생성
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        // 회원 정보 조회
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(response, EMAIL, AGE);

        // 회원 정보 수정
        ExtractableResponse<Response> modifyResponse = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        회원_정보_수정됨(modifyResponse);

        // 회원 정보 삭제
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        회원_삭제됨(deleteResponse);
    }


    @DisplayName("나의 정보를 조회한다.")
    @Test
    void getMemberMe() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 나의_정보_조회_요청(tokenResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMemberMe() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 나의_정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정됨(response);
    }

    @DisplayName("나의 정보를 삭제한다.")
    @Test
    void deleteMemberMe() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);


        // when
        ExtractableResponse<Response> response = 나의_정보_삭제_요청(tokenResponse);

        // then
        회원_삭제됨(response);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // 가입
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // 나의 정보 조회
        ExtractableResponse<Response> createResponse = 나의_정보_조회_요청(tokenResponse);
        회원_정보_조회됨(createResponse, EMAIL, AGE);


        // 회원 정보 수정
        ExtractableResponse<Response> modifyResponse = 나의_정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(modifyResponse);


        // 회원 정보 삭제
        ExtractableResponse<Response> deleteResponse = 나의_정보_삭제_요청(tokenResponse);
        회원_삭제됨(deleteResponse);
    }
}