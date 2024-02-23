package nextstep.core.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.annotation.AcceptanceTest;
import nextstep.core.member.domain.Member;
import nextstep.core.member.domain.MemberRepository;
import nextstep.core.station.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.common.utils.HttpResponseUtils.getCreatedLocationId;
import static nextstep.core.line.fixture.LineFixture.*;
import static nextstep.core.line.fixture.LineFixture.사호선;
import static nextstep.core.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.station.step.StationSteps.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
public class FavoriteAcceptanceTest {

    Long 교대역;
    Long 강남역;
    Long 양재역;
    Long 남부터미널역;
    Long 정왕역;
    Long 오이도역;

    Long 존재하지_않는_역 = 999L;

    Long 이호선;
    Long 신분당선;
    Long 삼호선;
    Long 사호선;

    String 정상적인_회원의_토큰;
    String 비정상적인_회원의_토큰 = "Bearer InValid Token";

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     * <p>
     * 오이도역 --- *4호선* --- 정왕역
     */
    @BeforeEach
    public void 사전_노선_설정() {
        교대역 = 지하철_역_생성(StationFixture.교대역);
        강남역 = 지하철_역_생성(StationFixture.강남역);
        양재역 = 지하철_역_생성(StationFixture.양재역);
        남부터미널역 = 지하철_역_생성(StationFixture.남부터미널역);
        정왕역 = 지하철_역_생성(StationFixture.정왕역);
        오이도역 = 지하철_역_생성(StationFixture.오이도역);

        이호선 = 지하철_노선_생성(이호선(교대역, 강남역, 10));
        신분당선 = 지하철_노선_생성(신분당선(강남역, 양재역, 10));
        삼호선 = 지하철_노선_생성(삼호선(교대역, 남부터미널역, 2));
        사호선 = 지하철_노선_생성(사호선(정왕역, 오이도역, 10));

        성공하는_지하철_구간_추가요청(삼호선, 지하철_구간(남부터미널역, 양재역, 3));
    }

    @BeforeEach
    void 사전_정상적인_회원의_토큰_발급() {
        String email = "admin@email.com";
        String password = "password";
        Integer age = 20;

        memberRepository.save(new Member(email, password, age));

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> 토큰_발급_요청 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        정상적인_회원의_토큰 = String.format("Bearer %s", 토큰_발급_요청.jsonPath().getString("accessToken"));
        assertThat(정상적인_회원의_토큰).isNotBlank();
    }

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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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
                        .header("Authorization", 정상적인_회원의_토큰)
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
                    String 출발역_번호 = String.valueOf(교대역);
                    String 도착역_번호 = String.valueOf(강남역);
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
                    String 출발역_번호 = String.valueOf(교대역);
                    String 도착역_번호 = String.valueOf(강남역);
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 비정상적인_회원의_토큰)
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
                    String 존재하지_않는_출발역_번호 = "999";
                    String 도착역_번호 = String.valueOf(강남역);

                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 존재하지_않는_출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
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
                    String 출발역_번호 = String.valueOf(교대역);
                    String 존재하지_않는_도착역_번호 = "999";

                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 존재하지_않는_도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
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
                    String 출발역_번호 = String.valueOf(교대역);
                    String 도착역_번호 = "1";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
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
                    String 출발역_번호 = String.valueOf(교대역);
                    String 도착역_번호 = "11";
                    Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                    경로_조회_요청_맵.put("source", 출발역_번호);
                    경로_조회_요청_맵.put("target", 도착역_번호);

                    // when, then
                    ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(경로_조회_요청_맵)
                            .when()
                            .post("/favorites")
                            .then().log().all()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract();

                    ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                            .given().log().all()
                            .header("Authorization", 정상적인_회원의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .when()
                        .get("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(Long.valueOf(출발역_번호));
                assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(Long.valueOf(도착역_번호));

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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 비정상적인_회원의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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

            String 정상적인_회원A의_토큰;
            String 정상적인_회원B의_토큰;

            @BeforeEach
            void 사전_정상적인_회원A의_토큰_발급() {
                String email = "admin003@email.com";
                String password = "password";
                Integer age = 20;

                memberRepository.save(new Member(email, password, age));

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                ExtractableResponse<Response> 토큰_발급_요청 = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().post("/login/token")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value()).extract();

                정상적인_회원A의_토큰 = String.format("Bearer %s", 토큰_발급_요청.jsonPath().getString("accessToken"));
                assertThat(정상적인_회원의_토큰).isNotBlank();
            }

            @BeforeEach
            void 사전_정상적인_회원B의_토큰_발급() {
                String email = "admin004@email.com";
                String password = "password";
                Integer age = 20;

                memberRepository.save(new Member(email, password, age));

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                ExtractableResponse<Response> 토큰_발급_요청 = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(params)
                        .when().post("/login/token")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value()).extract();

                정상적인_회원B의_토큰 = String.format("Bearer %s", 토큰_발급_요청.jsonPath().getString("accessToken"));
                assertThat(정상적인_회원의_토큰).isNotBlank();
            }
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 다른 사용자의 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패한다.
             */
            @Test
            void 다른_사용자의_즐겨찾기_삭제() {
                // given
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원A의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원B의_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원A의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                Long 존재하지_않는_즐겨찾기_번호 = 999L;

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", 존재하지_않는_즐겨찾기_번호))
                        .then().log().all()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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
                        .header("Authorization", 정상적인_회원의_토큰)
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
                String 출발역_번호 = String.valueOf(교대역);
                String 도착역_번호 = String.valueOf(강남역);
                Map<String, String> 경로_조회_요청_맵 = new HashMap<>();
                경로_조회_요청_맵.put("source", 출발역_번호);
                경로_조회_요청_맵.put("target", 도착역_번호);

                // when, then
                ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(경로_조회_요청_맵)
                        .when()
                        .post("/favorites")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 비정상적인_회원의_토큰)
                        .when()
                        .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                        .given().log().all()
                        .header("Authorization", 정상적인_회원의_토큰)
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