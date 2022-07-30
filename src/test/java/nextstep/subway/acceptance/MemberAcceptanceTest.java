package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AdministratorInfo.ADMIN_EMAIL;
import static nextstep.subway.acceptance.AdministratorInfo.ADMIN_PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    private String accessToken;
    private ExtractableResponse<Response> 회원생성_Response;
    @BeforeEach
    public void setUp(){
        super.setUp();
        accessToken = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);
        회원생성_Response = 회원_생성_요청(accessToken, EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when

        // then
        assertThat(회원생성_Response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(회원생성_Response);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(accessToken, 회원생성_Response, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(accessToken, 회원생성_Response);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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