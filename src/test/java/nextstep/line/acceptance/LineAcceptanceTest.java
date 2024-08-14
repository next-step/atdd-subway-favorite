package nextstep.line.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends LineAcceptanceFixture {
    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // Given & When
        Map<String, Object> 신분당선 = 신분당선_생성(신사역, 강남역);
        ExtractableResponse<Response> response = requestCreateLine(신분당선);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("모든 지하철 노션을 목록을 조회한다.")
    @Test
    void readLines() {
        // Given & When
        ExtractableResponse<Response> response = RestAssured.given().log().all().when().get("/lines").then().log().all()
                .extract();

        // Then
        var lineIds = response.jsonPath().getList("id", Long.class);
        assertThat(lineIds).containsExactlyInAnyOrder(신분당선, 분당선);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void readLine() {
        // Given
        var 신분당선 = requestCreateLine(신분당선_생성(신사역, 강남역)).jsonPath().getObject("id", Long.class);

        // When
        JsonPath jsonPath = RestAssured.given().log().all().when().get("/lines/" + 신분당선).then()
                .log().all().extract().jsonPath();

        // Then
        var findId = jsonPath.getObject("id", Long.class);
        var findName = jsonPath.getObject("name", String.class);
        assertThat(findId).isEqualTo(신분당선);
        assertThat(findName).isEqualTo("신분당선");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // Given
        var 신분당선 = requestCreateLine(신분당선_생성(신사역, 강남역)).jsonPath().getObject("id", Long.class);

        // When
        var updateParams = new HashMap<String, Object>();
        String UPDATE_신분당선 = "update 신분당선";
        String UPDATE_색깔 = "update-bg-red-600";
        updateParams.put("name", UPDATE_신분당선);
        updateParams.put("color", UPDATE_색깔);

        RestAssured.given().log().all().when()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + 신분당선)
                .then().log().all();

        // Then
        var jsonPath = RestAssured.given().log().all().when().get("/lines/" + 신분당선).then()
                .log().all().extract().jsonPath();
        var find_신분당선 = jsonPath.getObject("id", Long.class);
        var find_신분당선_이름 = jsonPath.getObject("name", String.class);
        var find_신분당선_색깔 = jsonPath.getObject("color", String.class);
        assertThat(find_신분당선).isEqualTo(신분당선);
        assertThat(find_신분당선_이름).isEqualTo(UPDATE_신분당선);
        assertThat(find_신분당선_색깔).isEqualTo(UPDATE_색깔);
    }

    @Test
    @DisplayName("지하철 노선의 이름을 업데이트한다.")
    void updateName() {
        // Given
        var 신분당선 = requestCreateLine(신분당선_생성(신사역, 강남역)).jsonPath().getObject("id", Long.class);

        // When
        var updateParams = new HashMap<String, Object>();
        var UPDATE_이름 = "update 신분당선";
        updateParams.put("name", UPDATE_이름);

        RestAssured.given().log().all().when()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + 신분당선)
                .then().log().all();

        // Then
        var jsonPath = RestAssured.given().log().all().when().get("/lines/" + 신분당선).then()
                .log().all().extract().jsonPath();
        var find_분당선 = jsonPath.getObject("id", Long.class);
        var find_분당선_이름 = jsonPath.getObject("name", String.class);
        assertThat(find_분당선).isEqualTo(신분당선);
        assertThat(find_분당선_이름).isEqualTo(UPDATE_이름);
    }

    @Test
    @DisplayName("지하철 노선의 색깔을 업데이트한다.")
    void updateColor() {
        // Given
        var 신분당선 = requestCreateLine(신분당선_생성(신사역, 강남역)).jsonPath().getObject("id", Long.class);

        // When
        var updateParams = new HashMap<String, Object>();
        var UPDATE_색깔 = "update-bg-red-600";
        updateParams.put("color", UPDATE_색깔);

        RestAssured.given().log().all().when()
                .body(updateParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + 신분당선)
                .then().log().all();

        // Then
        var jsonPath = RestAssured.given().log().all().when().get("/lines/" + 신분당선).then()
                .log().all().extract().jsonPath();
        var find_분당선 = jsonPath.getObject("id", Long.class);
        var find_분당선_색깔 = jsonPath.getObject("color", String.class);
        assertThat(find_분당선).isEqualTo(신분당선);
        assertThat(find_분당선_색깔).isEqualTo(UPDATE_색깔);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void deleteLine() {
        // Given
        var 신분당선 = requestCreateLine(신분당선_생성(신사역, 강남역)).jsonPath().getObject("id", Long.class);

        // When
        RestAssured.given().log().all()
                .when()
                .delete("/lines/" + 신분당선)
                .then().log().all();
        
        // Then
        ExtractableResponse<Response> response = RestAssured.given().log().all().when().get("/lines").then().log().all()
                .extract();

        var findLineID = response.jsonPath().getList("id", Long.class);
        assertThat(findLineID).doesNotContain(신분당선);
    }

    private static ExtractableResponse<Response> requestCreateLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
