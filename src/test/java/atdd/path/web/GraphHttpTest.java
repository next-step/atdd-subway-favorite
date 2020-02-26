package atdd.path.web;

import atdd.path.application.dto.PathResponseView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class GraphHttpTest {
    public static final String PATH_URL = "/paths";

    private HttpTestUtils httpTestUtils;

    public GraphHttpTest(HttpTestUtils httpTestUtils) {
        this.httpTestUtils = httpTestUtils;
    }

    public EntityExchangeResult<PathResponseView> findPath(long startId, long endId, String accessToken) {
        return httpTestUtils.getRequest(PATH_URL + "?startId=" + startId + "&endId=" + endId, accessToken, new ParameterizedTypeReference<PathResponseView>() {
        });
    }
}
