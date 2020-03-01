package atdd.path.web;

import atdd.path.application.dto.StationResponseView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

public class StationHttpTest {
    final String STATION_PATH = "/stations";
    public HttpTestUtils httpTestUtils;

    public StationHttpTest(HttpTestUtils httpTestUtils) {
        this.httpTestUtils = httpTestUtils;
    }

    public EntityExchangeResult<StationResponseView> createStationRequest(String stationName, String accessToken) {
        String inputJson = "{\"name\":\"" + stationName + "\"}";

        return httpTestUtils.postRequest(STATION_PATH, inputJson, accessToken, StationResponseView.class);
    }

    public EntityExchangeResult<StationResponseView> retrieveStationRequest(String uri, String accessToken) {
        return httpTestUtils.getRequest(uri, accessToken, new ParameterizedTypeReference<StationResponseView>() {
        });
    }

    public EntityExchangeResult<List<StationResponseView>> showStationsRequest(String accessToken) {
        return httpTestUtils.getRequest(STATION_PATH, accessToken, new ParameterizedTypeReference<List<StationResponseView>>() {
        });
    }

    public Long createStation(String stationName, String accessToken) {
        EntityExchangeResult<StationResponseView> stationResponse = createStationRequest(stationName, accessToken);
        return stationResponse.getResponseBody().getId();
    }

    public EntityExchangeResult<StationResponseView> retrieveStation(Long stationId, String accessToken) {
        return retrieveStationRequest(STATION_PATH + stationId, accessToken);
    }
}
