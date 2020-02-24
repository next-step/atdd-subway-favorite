package atdd.path.web;

import atdd.path.application.dto.LineResponseView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalTime;
import java.util.List;

public class LineHttpTest {
    public static final String LINE_URL = "/lines";

    private HttpTestUtils httpTestUtils;

    public LineHttpTest(HttpTestUtils httpTestUtils) {
        this.httpTestUtils = httpTestUtils;
    }

    public EntityExchangeResult<LineResponseView> createLineRequest(String lineName, String accessToken) {
        String inputJson = "{\"name\":\"" + lineName + "\"," +
                "\"startTime\":\"" + LocalTime.of(0, 0) + "\"," +
                "\"endTime\":\"" + LocalTime.of(23, 30) + "\"," +
                "\"interval\":\"" + 30 + "\"}";

        return httpTestUtils.postRequest(LINE_URL, inputJson, accessToken, LineResponseView.class);
    }

    public EntityExchangeResult<LineResponseView> retrieveLineRequest(long lineId, String accessToken) {
        return httpTestUtils.getRequest(LINE_URL + "/" + lineId, accessToken, new ParameterizedTypeReference<LineResponseView>() {
        });
    }

    public EntityExchangeResult<List<LineResponseView>> showLinesRequest(String accessToken) {
        return httpTestUtils.getRequest(LINE_URL, accessToken, new ParameterizedTypeReference<List<LineResponseView>>() {
        });
    }

    public Long createLine(String lineName, String accessToken) {
        EntityExchangeResult<LineResponseView> postResult = createLineRequest(lineName, accessToken);
        return postResult.getResponseBody().getId();
    }

    public EntityExchangeResult<LineResponseView> retrieveLine(Long lineId, String accessToken) {
        return retrieveLineRequest(lineId, accessToken);
    }

    public EntityExchangeResult createEdgeRequest(Long lineId, Long stationId, Long stationId2, String accessToken) {
        int distance = 10;
        String inputJson = "{\"sourceId\":" + stationId +
                ",\"targetId\":" + stationId2 +
                ",\"distance\":" + distance + "}";

        return httpTestUtils.postRequest("/lines/" + lineId + "/edges", inputJson, accessToken);
    }

    public void deleteById(long lineId, String accessToken) {
        httpTestUtils.deleteRequest(LINE_URL + "/" + lineId, accessToken);

    }
}
