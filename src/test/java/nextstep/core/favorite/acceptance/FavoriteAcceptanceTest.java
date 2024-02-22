package nextstep.core.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.utils.HttpResponseUtils.getCreatedLocationId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Nested
    class 즐겨찾기_추가 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  즐겨찾기를 추가하면
             * Then  해당 회원의 즐겨찾기 목록에 추가된다.
             */
            @Test
            void 즐겨찾기_추가() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // then
                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.parseLong(도착역_번호));
            }
        }

        @Nested
        class 실패 {
            @Nested
            class 회원_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     회원 정보를 전달하지 않은 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 회원정보_없이_즐겨찾기_추가() {
                    // given
                    String 출발역_번호 = "1";
                    String 도착역_번호 = "3";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .extract();
                }

                /**
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 회원 정보를 전달한 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 존재하지_않는_회원_정보로_즐겨찾기_추가() {
                    // given
                    String 존재하지_않는_회원의_베어러_토큰 = "Bearer Invalid Token";

                    String 출발역_번호 = "1";
                    String 도착역_번호 = "3";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 존재하지_않는_회원의_베어러_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .extract();
                }
            }

            @Nested
            class 경로_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 출발역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 존재하지_않는_출발역으로_즐겨찾기_추가() {
                    // given
                    String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                    String 존재하지_않는_출발역_번호 = "999";
                    String 도착역_번호 = "3";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 존재하지_않는_출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .when()
                            .get("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value())
                            .extract();

                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(Long.parseLong(존재하지_않는_출발역_번호));
                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(Long.parseLong(도착역_번호));
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 도착역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 존재하지_않는_도착역으로_즐겨찾기_추가() {
                    // given
                    String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                    String 출발역_번호 = "1";
                    String 존재하지_않는_도착역_번호 = "999";

                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 존재하지_않는_도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .when()
                            .get("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value())
                            .extract();

                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(Long.parseLong(출발역_번호));
                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(Long.parseLong(존재하지_않는_도착역_번호));
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 동일할 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 출발역과_도착역이_동일하게_즐겨찾기_추가() {
                    // given
                    String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                    String 출발역_번호 = "1";
                    String 도착역_번호 = "1";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .when()
                            .get("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value())
                            .extract();

                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(Long.parseLong(출발역_번호));
                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(Long.parseLong(도착역_번호));
                }

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 연결되지 않았을 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
                @Test
                void 출발역과_도착역이_연결되지_않은_즐겨찾기_추가() {
                    // given
                    String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                    String 출발역_번호 = "1";
                    String 도착역_번호 = "11";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_베어러_토큰)
                            .when()
                            .get("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value())
                            .extract();

                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(Long.parseLong(출발역_번호));
                    assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(Long.parseLong(도착역_번호));
                }
            }
        }
    }

    @Nested
    class 즐겨찾기_조회 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청하면
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 있다.
             */
            @Test
            void 추가한_즐겨찾기_조회() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().get("source.id").toString()).isEqualTo(출발역_번호);
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().get("target.id").toString()).isEqualTo(도착역_번호);

            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 없다.
             */
            @Test
            void 회원정보를_전달하지_않은_즐겨찾기_조회() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     존재하지 않는 회원 정보를 전달한 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 없다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_조회() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";
                String 존재하지_않는_회원의_베어러_토큰 = "Bearer Invalid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 존재하지_않는_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();
            }
        }
    }

    @Nested
    class 즐겨찾기_삭제 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 목록에서 삭제된다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_조회() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(Long.parseLong(도착역_번호));
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 다른 사용자의 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 다른_사용자의_즐겨찾기_삭제() {
                // given
                String 정상적인_A_회원의_베어러_토큰 = "Bearer Valid Token_A";
                String 정상적인_B_회원의_베어러_토큰 = "Bearer Valid Token_B";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_A_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_B_회원의_베어러_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_A_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.parseLong(도착역_번호));
            }


            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 존재하지 않는 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 존재하지_않는_즐겨찾기_삭제() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);

                Long 존재하지_않는_즐겨찾기_번호 = 999L;

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", 존재하지_않는_즐겨찾기_번호))
                        .then().log().all()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.parseLong(도착역_번호));
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 회원정보_없이_즐겨찾기_삭제() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.parseLong(도착역_번호));
            }

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     존재하지 않는 회원 정보를 전달한 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 존재하지_않는_회원정보로_즐겨찾기_삭제() {
                // given
                String 정상적인_회원의_베어러_토큰 = "Bearer Valid Token";
                String 존재하지_않는_회원의_베어러_토큰 = "Bearer Invalid Token";

                String 출발역_번호 = "1";
                String 도착역_번호 = "3";
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 존재하지_않는_회원의_베어러_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_베어러_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.parseLong(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.parseLong(도착역_번호));
            }
        }
    }
}