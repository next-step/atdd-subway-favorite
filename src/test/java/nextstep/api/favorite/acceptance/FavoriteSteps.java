package nextstep.api.favorite.acceptance;

import static nextstep.utils.AcceptanceHelper.statusCodeShouldBe;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import nextstep.api.favorite.application.dto.FavoriteRequest;
import nextstep.api.favorite.application.dto.FavoriteResponse;

public class FavoriteSteps {

    public static final String BASE_URL = "/favorites";

    public static ValidatableResponse 즐겨찾기_생성을_요청한다(final String token, final Long source, final Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token)
                .body(new FavoriteRequest(source, target))
                .when().post(BASE_URL)
                .then().log().all();
    }

    public static void 즐겨찾기_생성에_성공한다(final String token, final Long source, final Long target) {
        final var response = 즐겨찾기_생성을_요청한다(token, source, target);

        statusCodeShouldBe(response, HttpStatus.CREATED);
    }

    public static ValidatableResponse 모든_즐겨찾기_조회를_요청한다(final String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when().get(BASE_URL)
                .then();
    }

    public static List<FavoriteResponse> 모든_즐겨찾기_조회에_성공한다(final String token) {
        final var response = 모든_즐겨찾기_조회를_요청한다(token);

        statusCodeShouldBe(response, HttpStatus.OK);

        return response.extract()
                .jsonPath()
                .getList("", FavoriteResponse.class);
    }

    public static ValidatableResponse 즐겨찾기_제거를_요청한다(final String token, final Long id) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when().delete(BASE_URL + "/" + id)
                .then();
    }

    public static void 즐겨찾기_제거에_성공한다(final String token, final Long id) {
        final var response = 즐겨찾기_제거를_요청한다(token, id);

        statusCodeShouldBe(response, HttpStatus.NO_CONTENT);
    }
}
