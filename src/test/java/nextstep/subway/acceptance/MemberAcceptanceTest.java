package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.DataLoader.MEMBER_EMAIL;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void join() {
        // when
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        String location = createResponse.header("location");

        // then
        회원_정보_일치함(location, EMAIL, AGE);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        String location = createResponse.header("location");

        // when
        String newEmail = "new" + EMAIL;
        int newAge = AGE + 1;
        회원_정보_수정_요청(location, newEmail, PASSWORD, newAge);

        // then
        회원_정보_일치함(location, newEmail, newAge);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(MEMBER_EMAIL, PASSWORD, AGE);
        String location = createResponse.header("location");

        // when
        회원_삭제_요청(location);

        // then
        회원_정보를_조회할_수_없다(location);
    }

    private void 회원_정보를_조회할_수_없다(String location) {
        var response = 회원_정보_조회_요청(location);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}