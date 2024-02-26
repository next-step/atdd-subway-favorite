package nextstep.core.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.annotation.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.core.member.fixture.GithubMemberFixture.getAllGitHubMembers;
import static nextstep.core.member.step.AuthSteps.성공하는_토큰_발급_요청;
import static nextstep.core.member.step.AuthSteps.실패하는_토큰_발급_요청;
import static nextstep.core.member.step.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("인증 인수 테스트")
@AcceptanceTest
class AuthAcceptanceTest {

    @Nested
    class 토큰_발급 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생선한다.
             * When  생성된 회원 정보로 토큰 발급을 요청할 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 저장된_회원으로_토큰_발급_요청() {
                // given
                var 이메일 = "admin@email.com";
                var 비밀번호 = "password";

                회원_생성_요청(이메일, 비밀번호, 20);

                // when
                var 발급된_토큰 = 성공하는_토큰_발급_요청(이메일, 비밀번호);

                // then
                토큰_확인(발급된_토큰);
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 비밀번호가 아닌 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 비밀번호가_다른_회원의_토큰_발급_요청() {
                // given
                var 이메일 = "admin@email.com";
                var 비밀번호 = "password";
                var 변경된_비밀번호 = "changed password";

                회원_생성_요청(이메일, 비밀번호, 20);

                // then
                실패하는_토큰_발급_요청(이메일, 변경된_비밀번호);
            }

            /**
             * When  회원 저장소에 존재하지 않는 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 존재하지_않는_회원의_토큰_발급_요청() {
                // given
                var 이메일 = "admin@email.com";
                var 비밀번호 = "password";

                // when, then
                실패하는_토큰_발급_요청(이메일, 비밀번호);
            }
        }
    }

    /**
     * GitHub Server ----------------------------------------->
     * 1. 코드로 토큰 요청 -> 토큰을 응답
     * 2. 토큰으로 리소스 요청 -> 리소스 응답
     * <p>
     * My Server --------------------------------------------->
     * 1. 리소스로 사용자 저장 및 사용자 조회 요청 -> 사용자 조회 응답
     * 2. 액세스 토큰 발급
     */
    @Nested
    class 깃허브_로그인_요청 {
        @Nested
        class 성공 {
            @Nested
            class 가입된_회원 {
                /**
                 * Given 깃허브를 통해 회원가입된 회원을 생성한다.
                 * When  깃허브로 로그인 요청을 경우
                 * Then  깃허브로 로그인 된다.
                 */
                @Test
                void 깃허브로_회원가입된_회원의_로그인_요청() {

                    getAllGitHubMembers().forEach(githubMember -> {
                        // given
                        String 발급된_코드 = githubMember.getCode();

                        Map<String, String> params = new HashMap<>();
                        params.put("code", 발급된_코드);

                        // when
                        ExtractableResponse<Response> 토큰_응답 = RestAssured
                                .given().log().all()
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .body(params)
                                .when()
                                .post("/login/github")
                                .then().log().all()
                                .statusCode(HttpStatus.OK.value())
                                .extract();

                        // then
                        토큰_확인(토큰_응답.jsonPath().getString("accessToken"));

                    });
                }
            }

            @Nested
            class 가입되지_않은_회원 {
                /**
                 * Given 깃허브를 통해 회원가입 되지 않은 회원을 생성한다.
                 * When  깃허브로 로그인 요청을 경우
                 * Then  회원가입 후 깃허브로 로그인 된다.
                 */
                @Test
                void 깃허브로_회원가입_되지_않은_회원의_로그인_요청() {
                    getAllGitHubMembers().forEach(githubMember -> {
                        // given
                        String 발급된_코드 = githubMember.getCode();

                        Map<String, String> params = new HashMap<>();
                        params.put("code", 발급된_코드);

                        // when
                        ExtractableResponse<Response> 토큰_응답 = RestAssured
                                .given().log().all()
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .body(params)
                                .when()
                                .post("/login/github")
                                .then().log().all()
                                .statusCode(HttpStatus.OK.value())
                                .extract();

                        // then
                        토큰_확인(토큰_응답.jsonPath().getString("accessToken")); // TODO: Steps에서 공통 메서드로 추출
                    });
                }
            }
        }
    }

    public static void 토큰_확인(String 발급된_토큰) {
        assertThat(발급된_토큰).isNotBlank();
    }
}