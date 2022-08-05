package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.내_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.내_정보_일치함;
import static nextstep.subway.acceptance.MemberSteps.내_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.내_정보_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "test@test.com";
    private static final String NEW_EMAIL = "new@test.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final Integer NEW_AGE = 26;

    @DisplayName("회원가입을 한다.")
    @Test
    void join() {
        // when
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // then
        내_정보_일치함(accessToken, EMAIL, AGE);
    }

    @DisplayName("내 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        내_정보_수정_요청(accessToken, NEW_EMAIL, NEW_AGE);
        String newAccessToken = 로그인_되어_있음(NEW_EMAIL, PASSWORD);

        // then
        내_정보_일치함(newAccessToken, NEW_EMAIL, NEW_AGE);
    }

    @DisplayName("내 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        내_정보_삭제_요청(accessToken);

        // then
        내_정보를_조회할_수_없다(accessToken);
    }

    private void 내_정보를_조회할_수_없다(String accessToken) {
        var response = 내_정보_조회_요청(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}