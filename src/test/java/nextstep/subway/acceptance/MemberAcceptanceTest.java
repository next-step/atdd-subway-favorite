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


    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> 회원_생성_요청_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(회원_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> 회원_정보_조회_요청_응답 = 회원_정보_조회_요청(회원_생성_요청_응답);

        // then
        회원_정보_조회됨(회원_정보_조회_요청_응답, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 회원_정보_수정_요청_응답 =
                회원_정보_수정_요청(회원_생성_요청_응답, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(회원_정보_수정_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> 회원_삭제_요청_응답 = 회원_삭제_요청(회원_생성_요청_응답);

        // then
        회원_삭제됨(회원_삭제_요청_응답);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

        // when
        ExtractableResponse<Response> 내_회원_정보_수정_요청_응답 =
                내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(내_회원_정보_수정_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> 내_회원_삭제_요청_응답 = 내_회원_삭제_요청(accessToken);

        // then
        회원_삭제됨(내_회원_삭제_요청_응답);
    }
}