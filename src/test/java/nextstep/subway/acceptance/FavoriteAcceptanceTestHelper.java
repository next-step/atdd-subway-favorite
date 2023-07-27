package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.AcceptanceTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTestHelper extends PathAcceptanceTestHelper {

    protected static final String FAVORITE_RESOURCE_URL = "/favorites";


    protected ValidatableResponse createFavorite(Long sourceStationId, Long targetStationId, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        return createResource(FAVORITE_RESOURCE_URL, params, token);
    }

    protected ValidatableResponse getFavorite(Long favoriteId, String token) {
        return getResource(String.format("%s/%d", FAVORITE_RESOURCE_URL, favoriteId), token);
    }

    protected ValidatableResponse deleteFavorite(Long favoriteId) {
        return deleteResource(String.format("%s/%d", FAVORITE_RESOURCE_URL, favoriteId));
    }

    protected void verifyFoundFavorite(ValidatableResponse foundPathResponse, String... stationNames) {
        JsonPath jsonPath = foundPathResponse.extract().jsonPath();
        assertThat(jsonPath.getList("stationResponses.name", String.class)).containsExactly(stationNames);
    }
}
