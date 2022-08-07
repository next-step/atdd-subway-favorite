package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.DataLoader;
import nextstep.DataLoaderBootstrap;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        dataLoader.loadData();
    }

    public static ExtractableResponse<Response> get(final String url, final String token) {
        return AuthSteps.given(token)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String url, final String token, final Map<String, Object> params) {
        return AuthSteps.given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(final String url, final String token, final Map<String, Object> params) {
        return AuthSteps.given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(final String url, final String token) {
        return AuthSteps.given(token)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
