package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;


    @DisplayName("Feature : 회원 정보를 관리한다.")
    @Nested
    class Feature_ManageMbmer{
        @DisplayName("Scenario : 회원 정보를 관리한다.")
        @Test
        void manageMember() {
            //When
            ExtractableResponse<Response> 회원_생성_응답  = 회원_생성_요청(EMAIL,PASSWORD,AGE);
            //Then
            회원_생성됨(회원_생성_응답);
            //When
            ExtractableResponse<Response> 회원_조회_응답 = 회원_정보_조회_요청(회원_생성_응답);
            //Then
            회원_정보_조회됨(회원_조회_응답,EMAIL,AGE);
            //When
            ExtractableResponse<Response> 회원_수정_응답 = 회원_정보_수정_요청(회원_생성_응답,NEW_EMAIL,NEW_PASSWORD,NEW_AGE);
            //Then
            회원_정보_수정됨(회원_수정_응답);
            //When
            ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);
            //Then
            회원_삭제됨(회원_삭제_응답);

        }

        @DisplayName("Scenario : 나의 정보를 관리한다.")
        @Test
        void manageMyInfo() {
            //When
            ExtractableResponse<Response> 회원_생성_응답  = 회원_생성_요청(EMAIL,PASSWORD,AGE);
            //Then
            회원_생성됨(회원_생성_응답);
            //When
            TokenResponse 토큰 = 로그인_되어_있음(EMAIL,PASSWORD);
            //When
            ExtractableResponse<Response> 내_회원_조회_응답 = 내_회원_정보_조회_요청(토큰);
            //Then
            회원_정보_조회됨(내_회원_조회_응답,EMAIL,AGE);
            //When
            ExtractableResponse<Response> 내_회원_수정_응답 = 내_회원_정보_수정_요청(토큰,NEW_EMAIL,NEW_PASSWORD,NEW_AGE);
            회원_정보_수정됨(내_회원_수정_응답);
            //When
            ExtractableResponse<Response>  내_회원_삭제_응답 = 내_회원_정보_삭제_요청(토큰);
            //Then
            회원_삭제됨(내_회원_삭제_응답);
        }
    }



}