package nextstep.core.favorite.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.favorite.application.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.common.utils.HttpResponseUtils.getCreatedLocationId;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {


    public static ExtractableResponse<Response> 성공하는_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        return RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static void 잘못된_토큰으로_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 토큰없이_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보) {
        ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 실패하는_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_추가_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    public static void 특정_회원의_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(확인할_즐겨찾기_정보.getTarget());
    }

    public static void 토큰없이_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보) {
        ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                .given().log().all()
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getTarget());
    }

    public static void 잘못된_토큰으로_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                .given().log().all()
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();

        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getTarget());
    }

    public static void 특정_회원의_즐겨찾기_목록_없음_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_조회_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getTarget());
    }

    public static void 성공하는_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static void 실패하는_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 실패하는_존재하지_않는_즐겨찾기_삭제_요청(Long 즐겨찾기_번호, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .delete(String.format("/favorites/%d", 즐겨찾기_번호))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    public static void 토큰없이_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .when()
                .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 잘못된_토큰으로_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }
}
