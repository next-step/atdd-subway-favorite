package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.utils.AcceptanceTestUtils;

import java.util.HashMap;
import java.util.Map;

class StationAcceptanceTestHelper {

    protected static final String STATIONS_RESOURCE_URL = "/stations";

    protected ValidatableResponse createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return AcceptanceTestUtils.createResource(STATIONS_RESOURCE_URL, params);
    }
}
