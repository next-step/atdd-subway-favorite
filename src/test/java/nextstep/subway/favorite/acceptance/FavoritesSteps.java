package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesSteps {

    public static final String BASE_URL = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse memberToken, Long source, Long target) {
        return  RestAssured.given().log().all()
                .auth().oauth2(memberToken.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source, target))
                .when().post(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회를_요청(TokenResponse memberToken) {
        return  RestAssured.given().log().all()
                .auth().oauth2(memberToken.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse memberToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return  RestAssured.given().log().all()
                .auth().oauth2(memberToken.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_목록_내용_일치함(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {

        List<FavoriteResponse> responseList = response.jsonPath().getList(".", FavoriteResponse.class);

        responseList.forEach(favoriteResponse -> {
            assertThat(favoriteResponse.getSource().getId()).isEqualTo(source.getId());
            assertThat(favoriteResponse.getTarget().getId()).isEqualTo(target.getId());
        });
    }

    public static void 중복된_즐겨찾기_등록_실패됨(ExtractableResponse<Response> equalsFavoriteResponse) {
        assertThat(equalsFavoriteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_목록_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
