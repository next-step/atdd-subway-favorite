package atdd.path.web;

import atdd.path.AbstractHttpTest;
import atdd.path.application.dto.LineResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.List;

import static atdd.path.TestConstant.LINE_URL;

public class LineHttpTest extends AbstractHttpTest {

    public LineHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    public EntityExchangeResult<LineResponseView> createLineRequest(String lineName) {
        String inputJson = "{\"name\":\"" + lineName + "\"," +
                "\"startTime\":\"" + LocalTime.of(0, 0) + "\"," +
                "\"endTime\":\"" + LocalTime.of(23, 30) + "\"," +
                "\"interval\":\"" + 30 + "\"}";

        return createRequest(LineResponseView.class, LINE_URL, inputJson);
    }

    public EntityExchangeResult<LineResponseView> retrieveLineRequest(String uri) {
        return findRequest(LineResponseView.class, uri);
    }

    public EntityExchangeResult<List<LineResponseView>> showLinesRequest() {
        return findRequestList(LineResponseView.class, LINE_URL);
    }

    public Long createLine(String lineName) {
        EntityExchangeResult<LineResponseView> postResult = createLineRequest(lineName);
        return postResult.getResponseBody().getId();
    }

    public EntityExchangeResult<LineResponseView> retrieveLine(Long lineId) {
        return retrieveLineRequest(LINE_URL + "/" + lineId);
    }

    public EntityExchangeResult createEdgeRequest(Long lineId, Long stationId, Long stationId2) {
        int distance = 10;
        String inputJson = "{\"sourceId\":" + stationId +
                ",\"targetId\":" + stationId2 +
                ",\"distance\":" + distance + "}";

        return webTestClient.post().uri(LINE_URL + "/" + lineId + "/edges")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();
    }
}
