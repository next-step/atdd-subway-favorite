package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.dto.MemberResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.acceptance.commonStep.MemberSteps.*;
import static nextstep.acceptance.commonStep.TokenStep.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> response = 회원_정보_조회_요청_엑세스토큰(accessToken);
        var memberResponse = response.as(MemberResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 올바르지 않은 토큰으로 내 정보를 조회하면
     * Then Bad Request 400 error가 발생한다
     */
    @DisplayName("올바르지 않은 토큰으로 내 정보를 조회한다.")
    @Test
    void getMyInfoWithInvalidToken() {
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String invalidAccessToken = 로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken")+"Invalid Text";

        //when
        ExtractableResponse<Response> response = 회원_정보_조회_요청_엑세스토큰(invalidAccessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
