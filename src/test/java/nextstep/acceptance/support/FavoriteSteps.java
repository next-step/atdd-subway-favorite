package nextstep.acceptance.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fixture.AuthFixture;
import nextstep.fixture.StationFixture;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static nextstep.fixture.AuthFixture.로그인된_상태;
import static nextstep.fixture.FieldFixture.데이터_생성_결과_로케이션;
import static nextstep.fixture.FieldFixture.즐겨찾기_도착지_아이디;
import static nextstep.fixture.FieldFixture.즐겨찾기_목록_내_첫번째_도착지_이름;
import static nextstep.fixture.FieldFixture.즐겨찾기_목록_내_첫번째_식별자_아이디;
import static nextstep.fixture.FieldFixture.즐겨찾기_목록_내_첫번째_출발지_이름;
import static nextstep.fixture.FieldFixture.즐겨찾기_목록_전체;
import static nextstep.fixture.FieldFixture.즐겨찾기_출발지_아이디;
import static nextstep.utils.JsonPathUtil.List로_추출;
import static nextstep.utils.JsonPathUtil.문자열로_추출;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteSteps {

    // ----- 요청 데이터 생성 -----
    public static Map<String, String> 즐겨찾기_등록_요청_데이터_생성(Long 출발역_id, Long 도착역_id) {
        Map<String, String> params = new HashMap<>();
        params.put(즐겨찾기_출발지_아이디.필드명(), String.valueOf(출발역_id));
        params.put(즐겨찾기_도착지_아이디.필드명(), String.valueOf(도착역_id));
        return params;
    }

    public static String 즐겨찾기_등록_결과_Location(ExtractableResponse<Response> 즐겨찾기_등록_결과) {
        return 즐겨찾기_등록_결과.header(데이터_생성_결과_로케이션.필드명());
    }


    // ----- API 요청 -----
    public static ExtractableResponse<Response> 로그인_된_상태에서_즐겨찾기_등록_요청(AuthFixture 인증_주체, Long 출발역_id, Long 도착역_id) {
        return given(로그인된_상태(인증_주체)).log().all()
                .body(즐겨찾기_등록_요청_데이터_생성(출발역_id, 도착역_id))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_상태에서_즐겨찾기_등록_요청(Long 출발역_id, Long 도착역_id) {
        return given().log().all()
                .body(즐겨찾기_등록_요청_데이터_생성(출발역_id, 도착역_id))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_된_상태에서_즐겨찾기_목록_조회_요청(AuthFixture 인증_주체) {
        return given(로그인된_상태(인증_주체)).log().all()
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_상태에서_즐겨찾기_조회_요청() {
        return given().log().all()
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_된_상태에서_즐겨찾기_삭제_요청(AuthFixture 인증_주체, String 등록된_즐겨찾기_Location) {
        return given(로그인된_상태(인증_주체)).log().all()
                .when().delete(등록된_즐겨찾기_Location)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_상태에서_즐겨찾기_삭제_요청(String 등록된_즐겨찾기_Location) {
        return given().log().all()
                .when().delete(등록된_즐겨찾기_Location)
                .then().log().all()
                .extract();
    }


    // ----- 요청 결과 검증 -----
    public static void 즐겨찾기_등록에_성공한다(ExtractableResponse<Response> 즐겨찾기_등록_결과) {
        assertThat(즐겨찾기_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록이_한개_조회됨(ExtractableResponse<Response> 즐겨찾기_조회_결과) {
        assertThat(List로_추출(즐겨찾기_조회_결과, 즐겨찾기_목록_전체)).hasSize(1);
    }

    public static void 즐겨찾기_목록_조회에_성공한다(ExtractableResponse<Response> 즐겨찾기_조회_결과) {
        assertThat(즐겨찾기_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록이_비어있음(ExtractableResponse<Response> 즐겨찾기_조회_결과) {
        assertThat(List로_추출(즐겨찾기_조회_결과, 즐겨찾기_목록_전체)).isEmpty();
    }

    public static void 즐겨찾기_목록_정보_조회됨(ExtractableResponse<Response> 즐겨찾기_조회_결과, StationFixture 출발지, StationFixture 도착지) {
        assertAll(
                () -> assertThat(문자열로_추출(즐겨찾기_조회_결과, 즐겨찾기_목록_내_첫번째_식별자_아이디)).isNotNull(),
                () -> assertThat(문자열로_추출(즐겨찾기_조회_결과, 즐겨찾기_목록_내_첫번째_출발지_이름)).isEqualTo(출발지.역_이름()),
                () -> assertThat(문자열로_추출(즐겨찾기_조회_결과, 즐겨찾기_목록_내_첫번째_도착지_이름)).isEqualTo(도착지.역_이름())
        );
    }

    public static void 즐겨찾기_삭제에_성공한다(ExtractableResponse<Response> 즐겨찾기_삭제_결과) {
        assertThat(즐겨찾기_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
