package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static nextstep.subway.utils.AcceptanceTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long 신사역Id;
    private Long 논현역Id;
    private Long 강남역Id;
    private Long 판교역Id;
    private Long 광교역Id;

    @BeforeEach
    void setUpData() {
        신사역Id = 역_생성_후_id_추출("신사역");
        논현역Id = 역_생성_후_id_추출("논현역");
        강남역Id = 역_생성_후_id_추출("강남역");
        판교역Id = 역_생성_후_id_추출("판교역");
        광교역Id = 역_생성_후_id_추출("광교역");
    }

    /**
     * Given 특정 노선이 등록돼있고
     * When 관리자가 해당 노선에 새로운 구간을 추가하면
     * Then 해당 노선에 새로운 구간이 추가된다.
     */
    @Test
    @DisplayName("노선에 새로운 구간 추가")
    void 구간등록_case1() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(강남역Id, 판교역Id, 10L);


        // when
        ExtractableResponse<Response> response = 노선에_새로운_구간_추가_Extract(newSection, lineId);
        // then
        상태코드_CREATED(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(3);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
        assertThat(Long.parseLong(stations.get(2).get("id").toString())).isEqualTo(판교역Id);
    }

    /**
     * Given 특정 노선이 등록돼있고
     * When 관리자가 이미 노선에 등록된 역을 하행역으로 가진 새로운 구간을 추가하면
     * Then 에러가 발생한다.
     */
    @Test
    @DisplayName("노선에 이미 등록된 역을 하행역으로 가진 구간 추가시 예외 발생")
    void 구간등록_case3() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(강남역Id, 신사역Id, 10L);

        // when
        ExtractableResponse<Response> response = 노선에_새로운_구간_추가_Extract(newSection, lineId);

        // then
        상태코드_BAD_REQUEST(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 노선이 등록돼있고,
     * When 관리자가 노선의 마지막 구간을 삭제하면
     * Then 노선에서 구간이 삭제된다.
     */
    @Test
    @DisplayName("노선의 마지막 구간 삭제")
    void 구간삭제_case1() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(강남역Id, 판교역Id, 10L);

        노선에_새로운_구간_추가_Extract(newSection, lineId);

        // when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 판교역Id);

        // then
        상태코드_NO_CONTENT(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 노선이 등록돼있고
     * When 관리자가 노선의 구간이 아닌 다른 구간을삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    @DisplayName("노선의 구간이 아닌 다른 구간 삭제 시 예외 발생")
    void 구간삭제_case2() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(강남역Id, 판교역Id, 10L);

        노선에_새로운_구간_추가_Extract(newSection, lineId);

        // when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 광교역Id);

        // then
        상태코드_BAD_REQUEST(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(3);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
        assertThat(Long.parseLong(stations.get(2).get("id").toString())).isEqualTo(판교역Id);
    }

    /**
     * Given 구간이 하나뿐인 노선이 등록돼있고
     * When 관리자가 해당 노선의 구간을 삭제하면
     * Then 에러가 발생하고 해당 구간은 삭제되지않는다.
     */
    @Test
    @DisplayName("노선에 구간이 하나뿐일때 구간 삭제 시 예외 발생")
    void 구간삭제_case4() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        // when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 강남역Id);

        // then
        상태코드_BAD_REQUEST(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 구간이 1개(A-B) 등록된 노선이 있고,
     * When 구간 사이에 노선(A-C)을 추가하면
     * Then 구간 사이에 새로운 구간이 추가된다(A-C, C-B)
     */
    @Test
    @DisplayName("노선에 역 추가 시 노선 가운데 추가 할 수 있다.")
    void 노선_중간에_구간_추가() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(신사역Id, 논현역Id, 4L);

        // when
        ExtractableResponse<Response> response = 노선에_새로운_구간_추가_Extract(newSection, lineId);

        // then
        상태코드_CREATED(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(3);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(논현역Id);
        assertThat(Long.parseLong(stations.get(2).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 구간이 1개 등록된 노선이 있고,
     * When 노선 처음엔 새로운 구간을 추가하면
     * Then 노선의 맨 앞에 구간이 추가되고 구간은 총 2개가 된다.
     */
    @Test
    @DisplayName("노선에 역 추가 시 노선 처음에 추가 할 수 있다.")
    void 노선_맨_앞에_구간_추가() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 논현역Id, 강남역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);

        Map<String, Object> newSection = 구간_생성_매개변수(신사역Id, 논현역Id, 4L);

        // when
        ExtractableResponse<Response> response = 노선에_새로운_구간_추가_Extract(newSection, lineId);

        // then
        상태코드_CREATED(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(3);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(논현역Id);
        assertThat(Long.parseLong(stations.get(2).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 구간이 2개(A-B, B-C) 등록된 노선이 있고,
     * When 이미 등록된 역(A)이 포함된 구간(C-A)을 등록하면
     * Then 에러가 발생한다.
     */
    @Test
    @DisplayName("이미 등록되어있는 역은 노선에 등록될 수 없다.")
    void 중복된_역_등록_불가능() {
        // given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 논현역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(논현역Id, 강남역Id, 4L), lineId);

        // when
        ExtractableResponse<Response> response = 노선에_새로운_구간_추가_Extract(구간_생성_매개변수(강남역Id, 신사역Id, 4L), lineId);

        // then
        상태코드_BAD_REQUEST(response);
    }

    /**
     * Given 구간이 2개(A-B, B-C) 등록된 노선이 있고,
     * When 가운데 역(B)을 삭제하면
     * Then 가운데 역은 제거되고 양 옆의 역이 이어진 하나의 구간이 된다.(A-C)
     */
    @Test
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다.")
    void 가운데_구간_삭제() {
        //given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 논현역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(논현역Id, 강남역Id, 4L), lineId);

        //when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 논현역Id);

        //then
        상태코드_NO_CONTENT(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(신사역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
    }


    /**
     * Given 구간이 2개(A-B, B-C) 등록된 노선이 있고,
     * When 상행 종점역(A)을 삭제하면
     * Then 상행 종점역 구간은 제거된다.(B-C)
     */
    @Test
    @DisplayName("노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.")
    void 상행_종점_구간_삭제() {
        //given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 논현역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(논현역Id, 강남역Id, 4L), lineId);

        //when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 신사역Id);

        //then
        상태코드_NO_CONTENT(response);
        List<Map<String, Object>> stations = 노선_조회_Extract(lineId).jsonPath().getList("stations");
        assertThat(stations.size()).isEqualTo(2);
        assertThat(Long.parseLong(stations.get(0).get("id").toString())).isEqualTo(논현역Id);
        assertThat(Long.parseLong(stations.get(1).get("id").toString())).isEqualTo(강남역Id);
    }

    /**
     * Given 구간이 3개(A-B, B-C, C-D) 등록된 노선이 있고,
     * When 노선에 존자하지 않는 역(F)을 삭제하면
     * Then 에러가 발생한다.
     */
    @Test
    @DisplayName("노선에 등록되지 않는 구간 삭제 시 에러가 발생한다.")
    void 존재하지않는_구간_삭제() {
        //given
        Map<String, Object> 신분당선_생성_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 논현역Id, 10L);
        long lineId = 노선_생성_후_id_추출(신분당선_생성_매개변수);
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(논현역Id, 강남역Id, 4L), lineId);
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(강남역Id, 판교역Id, 5L), lineId);

        //when
        ExtractableResponse<Response> response = getSectionDeletionExtract(lineId, 광교역Id);

        //then
        상태코드_BAD_REQUEST(response);
    }

    private ExtractableResponse<Response> getSectionDeletionExtract(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
