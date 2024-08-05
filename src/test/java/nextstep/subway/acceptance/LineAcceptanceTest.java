package nextstep.subway.acceptance;

import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.line.dto.LineUpdateRequest;
import nextstep.subway.domain.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends BasicAcceptanceTest{

    private StationResponse 장암역;
    private StationResponse 석남역;
    private StationResponse 계양역;
    private StationResponse 송도달빛축제공원역;

    /**
     * 초기 지하철 역 생성
     */
    @BeforeEach
    void initStations() {
        장암역 = StationCommonApi.createStation("장암역").as(StationResponse.class);
        석남역 = StationCommonApi.createStation("석남역").as(StationResponse.class);
        계양역 = StationCommonApi.createStation("계양역").as(StationResponse.class);
        송도달빛축제공원역 = StationCommonApi.createStation("송도달빛축제공원역").as(StationResponse.class);
    }

    /**
     * 노선 생성 공통 메서드
     */
    private void initLines() {
        LineCreateRequest request = new LineCreateRequest("7호선", "bg-red-600", 장암역.getId(), 석남역.getId(), 10);
        LineCommonApi.createLine(request);
        request = new LineCreateRequest("인천1호선", "bg-blue-600", 계양역.getId(), 송도달빛축제공원역.getId(), 12);
        LineCommonApi.createLine(request);
    }

    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 등록한다.")
    @Test
    void createLine() {
        //given
        LineCreateRequest request = new LineCreateRequest("7호선", "bg-red-600", 장암역.getId(), 석남역.getId(), 10);

        var response = LineCommonApi.createLine(request);

        //when
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        LineResponse lineResponse = response.as(LineResponse.class);

        // stations 필드로 인해 내부 필드 일부를 검증
        assertThat(lineResponse.getId()).isEqualTo(장암역.getId());
        assertThat(lineResponse.getStations()).contains(new StationResponse(장암역.getId(), "장암역"), new StationResponse(석남역.getId(), "석남역"));
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findLines() {
        //given
        initLines();

        //when
        List<LineResponse> lines = LineCommonApi.findLines().jsonPath().getList(".", LineResponse.class);
        List<String> names = lines.stream().map(LineResponse::getName).collect(Collectors.toList());

        //then
        assertThat(names).containsExactly("7호선", "인천1호선");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다")
    @Test
    void findLine() {
        //given
        initLines();

        //when
        var response = LineCommonApi.findLineById(장암역.getId());
        LineResponse line = response.as(LineResponse.class);

        //then
        assertThat(line.getId()).isEqualTo(장암역.getId());
        assertThat(line.getName()).isEqualTo("7호선");
        assertThat(line.getStations()).containsExactly(new StationResponse(장암역.getId(), "장암역"), new StationResponse(석남역.getId(), "석남역"));
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void updateLine() {
        //given
        initLines();

        var response = LineCommonApi.findLineById(장암역.getId());
        LineResponse beforeLine = response.as(LineResponse.class);

        //when
        LineCommonApi.updateLine(beforeLine.getId(), new LineUpdateRequest("신 7호선", "bg-blue-600"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        LineResponse afterLine = LineCommonApi.findLineById(장암역.getId()).as(LineResponse.class);
        assertThat(beforeLine).isNotEqualTo(afterLine);
    }

    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteLine() {
        //given
        initLines();

        //when
        LineCommonApi.deleteLine(장암역.getId());

        //then
        var lines = LineCommonApi.findLines()
                .jsonPath()
                .getList("name", String.class);

        assertThat(lines).doesNotContain("7호선");
    }
}
