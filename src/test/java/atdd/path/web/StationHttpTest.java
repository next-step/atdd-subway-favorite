package atdd.path.web;

import atdd.path.AbstractHttpTest;
import atdd.path.application.dto.StationResponseView;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static atdd.path.TestConstant.STATION_URL;

public class StationHttpTest extends AbstractHttpTest {

    public StationHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    public EntityExchangeResult<StationResponseView> createStationRequest(String stationName) {
        String inputJson = "{\"name\":\"" + stationName + "\"}";

        return createRequest(StationResponseView.class, STATION_URL, inputJson);
    }

    public EntityExchangeResult<StationResponseView> retrieveStationRequest(String uri) {
        return findRequest(StationResponseView.class, uri);
    }

    public EntityExchangeResult<List<StationResponseView>> showStationsRequest() {
        return findRequestList(StationResponseView.class, STATION_URL);
    }

    public Long createStation(String stationName) {
        EntityExchangeResult<StationResponseView> stationResponse = createStationRequest(stationName);
        return stationResponse.getResponseBody().getId();
    }

    public EntityExchangeResult<StationResponseView> retrieveStation(Long stationId) {
        return retrieveStationRequest(STATION_URL + "/" + stationId);
    }
}
