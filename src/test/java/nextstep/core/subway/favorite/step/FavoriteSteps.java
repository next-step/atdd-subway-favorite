package nextstep.core.subway.favorite.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.subway.favorite.application.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.common.utils.HttpResponseUtils.getCreatedLocationId;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        return 즐겨찾기_추가_요청(즐겨찾기_요청_정보, 토큰, HttpStatus.CREATED);
    }

    public static void 잘못된_토큰으로_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        즐겨찾기_추가_요청(즐겨찾기_요청_정보, 토큰, HttpStatus.UNAUTHORIZED);
    }

    public static void 토큰없이_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보) {
        토큰_없이_즐겨찾기_추가_요청(즐겨찾기_요청_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 잘못된_경로로_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰) {
        즐겨찾기_추가_요청(즐겨찾기_요청_정보, 토큰, HttpStatus.BAD_REQUEST);
    }

    public static void 토큰기반_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        즐겨찾기_목록_포함_검증(확인할_즐겨찾기_정보, 즐겨찾기_목록_요청(토큰, HttpStatus.OK));
    }

    public static void 토큰없이_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보) {
        즐겨찾기_목록_미포함_검증(확인할_즐겨찾기_정보, 토큰_없이_즐겨찾기_목록_요청(HttpStatus.UNAUTHORIZED));
    }

    public static void 잘못된_토큰으로_즐겨찾기_목록_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        즐겨찾기_목록_미포함_검증(확인할_즐겨찾기_정보, 즐겨찾기_목록_요청(토큰, HttpStatus.UNAUTHORIZED));
    }

    public static void 특정_회원의_즐겨찾기_목록_없음_검증(FavoriteRequest 확인할_즐겨찾기_정보, String 토큰) {
        즐겨찾기_목록_미포함_검증(확인할_즐겨찾기_정보, 즐겨찾기_목록_요청(토큰, HttpStatus.OK));
    }

    public static void 즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰) {
        즐겨찾기_삭제_요청(즐겨찾기_추가_요청_응답, 토큰, HttpStatus.NO_CONTENT);
    }

    public static void 잘못된_토큰으로_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰) {
        즐겨찾기_삭제_요청(즐겨찾기_추가_요청_응답, 토큰, HttpStatus.UNAUTHORIZED);
    }

    public static void 즐겨찾기_삭제_요청(Long 즐겨찾기_번호, String 토큰) {
        즐겨찾기_삭제_요청(즐겨찾기_번호, 토큰, HttpStatus.BAD_REQUEST);
    }

    public static void 토큰없이_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답) {
        토큰없이_즐겨찾기_삭제_요청(즐겨찾기_추가_요청_응답, HttpStatus.UNAUTHORIZED);
    }

    public static void 즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, String 토큰, HttpStatus 응답코드) {
        즐겨찾기_삭제_요청(getCreatedLocationId(즐겨찾기_추가_요청_응답), 토큰, 응답코드);
    }

    private static ExtractableResponse<Response> 즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, String 토큰, HttpStatus 응답코드) {
        return RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static void 토큰_없이_즐겨찾기_추가_요청(FavoriteRequest 즐겨찾기_요청_정보, HttpStatus 응답코드) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(즐겨찾기_요청_정보)
                .when()
                .post("/favorites")
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static ExtractableResponse<Response> 즐겨찾기_목록_요청(String 토큰, HttpStatus 응답코드) {
        return RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static ExtractableResponse<Response> 토큰_없이_즐겨찾기_목록_요청(HttpStatus 응답코드) {
        return RestAssured
                .given().log().all()
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static void 즐겨찾기_삭제_요청(Long 즐겨찾기_번호, String 토큰, HttpStatus 응답코드) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .header("Authorization", 토큰)
                .when()
                .delete(String.format("/favorites/%d", 즐겨찾기_번호))
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static void 토큰없이_즐겨찾기_삭제_요청(ExtractableResponse<Response> 즐겨찾기_추가_요청_응답, HttpStatus 응답코드) {
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = RestAssured
                .given().log().all()
                .when()
                .delete(String.format("/favorites/%d", getCreatedLocationId(즐겨찾기_추가_요청_응답)))
                .then().log().all()
                .statusCode(응답코드.value())
                .extract();
    }

    private static void 즐겨찾기_목록_포함_검증(FavoriteRequest 확인할_즐겨찾기_정보, ExtractableResponse<Response> 즐겨찾기_조회_요청_응답) {
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).containsExactly(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).containsExactly(확인할_즐겨찾기_정보.getTarget());
    }

    private static void 즐겨찾기_목록_미포함_검증(FavoriteRequest 확인할_즐겨찾기_정보, ExtractableResponse<Response> 즐겨찾기_조회_요청_응답) {
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("source.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getSource());
        assertThat(즐겨찾기_조회_요청_응답.jsonPath().getList("target.id", Long.class)).doesNotContain(확인할_즐겨찾기_정보.getTarget());
    }
}
