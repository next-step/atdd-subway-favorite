package subway.acceptance.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptance.auth.AuthFixture;
import subway.acceptance.auth.AuthSteps;
import subway.utils.AcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원가입을 하면
     * Then 회원 가입이 된다.
     */
    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");

        // when
        var response = MemberSteps.회원_조회_API(memberLocation);

        // then
        assertThat(response.jsonPath().getString("id")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.jsonPath().getString("age")).isEqualTo(String.valueOf(AGE));
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);
        var loginResponse = AuthSteps.로그인_API(로그인);
        var accessToken = loginResponse.jsonPath().getString("accessToken");

        // when
        var response = MemberSteps.내_정보_조회_API(accessToken);

        // then
        assertThat(response.jsonPath().getString("id")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.jsonPath().getString("age")).isEqualTo(String.valueOf(AGE));
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 수정하면
     * Then 내 정보를 수정할 수 있다
     */
    @DisplayName("회원 정보를 수정한다.")
    @Test
    void changeMyInfo() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");

        // when
        final String newEmail = "new@gmail.com";
        Map<String, String> params = new HashMap<>();
        params.put("email", newEmail);
        params.put("password", PASSWORD);
        params.put("age", String.valueOf(AGE));
        MemberSteps.회원_수정_API(memberLocation, params);

        var response = MemberSteps.회원_조회_API(memberLocation);

        // then
        assertThat(response.jsonPath().getString("id")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(newEmail);
        assertThat(response.jsonPath().getString("age")).isEqualTo(String.valueOf(AGE));
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 나를 삭제 하면
     * Then 삭제 할 수 있다
     */
    @DisplayName("회원을 삭제한다.")
    @Test
    void deleteMyInfo() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");

        // when
        MemberSteps.회원_삭제_API(memberLocation);
        var response = MemberSteps.회원_조회_API(memberLocation);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


}