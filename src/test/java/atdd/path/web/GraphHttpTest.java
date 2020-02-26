package atdd.path.web;

import atdd.path.AbstractHttpTest;
import atdd.path.application.dto.PathResponseView;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.path.TestConstant.PATH_URL;

public class GraphHttpTest extends AbstractHttpTest {

    public GraphHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    public PathResponseView findPath(Long startId, Long endId) {
        EntityExchangeResult<PathResponseView> result = findPathRequest(startId, endId);
        return result.getResponseBody();
    }

    public EntityExchangeResult<PathResponseView> findPathRequest(Long startId, Long endId) {
        return findRequest(PathResponseView.class, PATH_URL + "?startId=" + startId + "&endId=" + endId);
    }

}
