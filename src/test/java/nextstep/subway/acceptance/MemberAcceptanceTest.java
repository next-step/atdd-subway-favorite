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

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(회원_생성_응답);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var 회원_생성됨 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var 회원_정보_조회_응답 = 회원_정보_조회_요청(회원_생성됨);

        // then
        회원_정보_조회됨(회원_정보_조회_응답, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var 회원_정보_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        회원_정보_수정됨(회원_정보_수정_응답);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);

        // then
        회원_삭제됨(회원_삭제_응답);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var accessToken = Access_Token을_가져온다(베어러_인증_로그인_응답);

        // when
        var OAuth2_인증으로_내_회원_정보_조회_응답 = OAuth2_인증으로_내_회원_정보_조회_요청(accessToken);
        // then
        회원_정보_조회됨(OAuth2_인증으로_내_회원_정보_조회_응답, EMAIL, AGE);
    }
}
