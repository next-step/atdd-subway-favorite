package nextstep;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.auth.acceptance.AuthSteps;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.subway.controller.dto.LineCreateRequest;
import nextstep.subway.controller.dto.StationCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static nextstep.DataSaver.MEMBER_EMAIL;
import static nextstep.DataSaver.MEMBER_PASSWORD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DataSaver dataSaver;

    protected String accessToken;

    @BeforeEach
    protected void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleaner.clear();
        dataSaver.saveData();
    }

    @BeforeEach
    void setup() {
        if(accessToken == null || accessToken.isEmpty()) {
            accessToken = AuthSteps.로그인_요청(MEMBER_EMAIL, MEMBER_PASSWORD)
                    .as(TokenResponse.class).getAccessToken();
        }
    }

    protected ExtractableResponse<Response> 지하철역_생성_요청(StationCreateRequest request, int statusCode) {
        return post("/stations", request, statusCode);
    }

    protected ExtractableResponse<Response> 노선_생성_요청(LineCreateRequest request, int statusCode) {
        return post("/lines", request, statusCode);
    }

    protected ExtractableResponse<Response> 노선_조회_요청(Long id, int statusCode) {
        return get("/lines/{id}", statusCode, new HashMap<>(), id);
    }

    protected ExtractableResponse<Response> get(String path, int statusCode, Map<String, ?> queryParams, Object... pathParams) {
        RequestSpecification requestSpecification = RestAssured.given().log().all();

        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(requestSpecification::queryParam);
        }

        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> post(String path, Object body, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> put(String path, Object body, int statusCode, Object... pathParams) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

    protected ExtractableResponse<Response> delete(String path, int statusCode, Map<String, ?> queryParams, Object... pathParams) {
        RequestSpecification requestSpecification = RestAssured.given().log().all();

        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.forEach(requestSpecification::queryParam);
        }

        return requestSpecification
                .when().delete(path, pathParams)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

}
