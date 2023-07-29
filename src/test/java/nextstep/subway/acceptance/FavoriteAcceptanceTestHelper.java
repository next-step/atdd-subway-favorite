package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.AcceptanceTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTestHelper extends PathAcceptanceTestHelper {

    protected static final String FAVORITE_RESOURCE_URL = "/favorites";

    protected ValidatableResponse createMember(String email, String password, int age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/members")
                .then().log().all();
    }

    protected ValidatableResponse getToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all();
    }


    protected ValidatableResponse createFavorite(Long sourceStationId, Long targetStationId, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        return createResource(FAVORITE_RESOURCE_URL, params, token);
    }

    protected ValidatableResponse getFavorite(Long favoriteId, String token) {
        return getResource(String.format("%s/%d", FAVORITE_RESOURCE_URL, favoriteId), token);
    }

    protected ValidatableResponse getFavorites(String token) {
        return getResource(FAVORITE_RESOURCE_URL, token);
    }

    protected ValidatableResponse deleteFavorite(Long favoriteId, String token) {
        return deleteResource(String.format("%s/%d", FAVORITE_RESOURCE_URL, favoriteId), token);
    }

    protected void verifyFoundFavorite(ValidatableResponse foundFavoriteResponse, String sourceName, String targetName) {
        JsonPath jsonPath = foundFavoriteResponse.extract().jsonPath();
        assertThat(jsonPath.getString("source.name")).isEqualTo(sourceName);
        assertThat(jsonPath.getString("target.name")).isEqualTo(targetName);
    }

    protected void verifyFoundFavorites(ValidatableResponse foundFavoriteResponse, String sourceName, String targetName) {
        JsonPath jsonPath = foundFavoriteResponse.extract().jsonPath();
        assertThat(jsonPath.getString("source.name[0]")).isEqualTo(sourceName);
        assertThat(jsonPath.getString("target.name[0]")).isEqualTo(targetName);
    }

}
