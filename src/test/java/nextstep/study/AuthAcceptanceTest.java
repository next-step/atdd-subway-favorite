package nextstep.study;

import static nextstep.study.AuthSteps.accesstoken_발급_검증;
import static nextstep.study.AuthSteps.깃헙_로그인;
import static nextstep.study.AuthSteps.상태코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 깃헙_로그인_요청시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_code로 {

            @ParameterizedTest
            @ValueSource(strings = {"aofijeowifjaoief", "fau3nfin93dmn", "afnm93fmdodf", "fm04fndkaladmd"})
            @DisplayName("로그인 요청하면 access token을 발급해준다")
            void 로그인_요청하면_access_token을_발급해준다(String code) {
                var response = 깃헙_로그인(code);

                // given
                accesstoken_발급_검증(response);
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않는_code로 {

            private final String 유효하지_않는_코드 = "aofijeowifjaoief1";

            @Test
            @DisplayName("로그인 요청하면 500 코드를 리턴한다")
            void access_token을_발급해준다() {
                var response = 깃헙_로그인(유효하지_않는_코드);

                // given
                상태코드_확인(response, 500);
            }
        }
    }
}
