package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.CommonAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends CommonAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /** given 회원 정보를 생성하여 토큰을 발급받고
     *  when 발급된 토큰으로 회원 정보를 요청하면
     *  then 회원 정보를 응답 받는다
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //회원 정보 생성
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        //회원 정보(이메일)로 토큰 발급 (회원 정보가 없으면 오류 발생)
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        //발급된 토큰으로 회원 정보 찾기
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        //토큰 발급 시 사용한 이메일과 응답 받은 회원 정보가 같은지 검증
        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }
}