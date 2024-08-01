package nextstep.subway.acceptance;

import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;
import static nextstep.subway.acceptance.step.LineStep.*;
import static nextstep.subway.acceptance.step.LineStepExtractor.노선_추출기;
import static nextstep.subway.acceptance.step.PathStep.경로를_조회한다;
import static nextstep.subway.acceptance.step.PathStepAsserter.거리가_최소값인지_검증한다;
import static nextstep.subway.acceptance.step.PathStepExtractor.경로_추출기;
import static nextstep.subway.acceptance.step.StationStep.*;
import static nextstep.subway.acceptance.step.StationStepAsserter.역_목록에_지정된_역들이_순서대로_포함되는지_검증한다;
import static nextstep.subway.acceptance.step.StationStepExtractor.역_추출기;

@DisplayName("지하철 경로 검색 인수 테스트")
class PathAcceptanceTest extends BaseTestSetup {
    private Long 시청역_id;
    private Long 서울역_id;

    private Long 을지로입구역_id;
    private Long 을지로3가역_id;

    private Long 회현역_id;
    private Long 명동역_id;
    private Long 충무로역_id;

    /**
     *           *2호선*
     * 시청역 --- 을지로입구역 --- 을지로3가역
     *  |                         \
     *  | *1호선*                   \ *3호선*
     *  |                           \
     * 서울역 --- 회현역 --- 명동역 --- 충무로역
     *              *4호선*
     */
    @BeforeEach
    public void setUp() {
        시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());
        을지로입구역_id = 역_추출기.단일_id_를_추출한다(을지로입구역을_생성한다());
        을지로3가역_id = 역_추출기.단일_id_를_추출한다(을지로3가역을_생성한다());
        회현역_id = 역_추출기.단일_id_를_추출한다(회현역을_생성한다());
        명동역_id = 역_추출기.단일_id_를_추출한다(명동역을_생성한다());
        충무로역_id = 역_추출기.단일_id_를_추출한다(충무로역을_생성한다());

        일호선을_생성한다(시청역_id, 서울역_id, 10L);

        Long 이호선_id = 노선_추출기.단일_id_를_추출한다(이호선을_생성한다(시청역_id, 을지로입구역_id, 10L));
        구간을_추가한다(이호선_id, 을지로입구역_id, 을지로3가역_id, 10L);

        삼호선을_생성한다(을지로3가역_id, 충무로역_id, 10L);

        Long 사호선_id = 노선_추출기.단일_id_를_추출한다(사호선을_생성한다(서울역_id, 회현역_id, 10L));
        구간을_추가한다(사호선_id, 회현역_id, 명동역_id, 10L);
        구간을_추가한다(사호선_id, 명동역_id, 충무로역_id, 10L);
    }

    /**
     * Given: 지하철 노선 및 구간들이 등록되어 있고
     * When: 등록되지 않은 출발역으로 경로를 조회하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 등록되어_있지_않은_출발역의_경로_조회시_오류_발생_테스트() {
        // given
        Long 삼성역_id = 2222L;

        // when
        var 경로_조회_응답값 = 경로를_조회한다(삼성역_id, 시청역_id);

        // then
        응답_상태값이_올바른지_검증한다(경로_조회_응답값, HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given: 지하철 노선 및 구간들이 등록되어 있고
     * When: 등록되지 않은 도착역으로 경로를 조회하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 등록되어_있지_않은_도착역의_경로_조회시_오류_발생_테스트() {
        // given
        Long 삼성역_id = 2222L;

        // when
        var 경로_조회_응답값 = 경로를_조회한다(시청역_id, 삼성역_id);

        // then
        응답_상태값이_올바른지_검증한다(경로_조회_응답값, HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given: 지하철 노선 및 구간들이 등록되어 있고
     * When: 출발역과 도착역이 동일한 경로를 조회하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 출발역과_도착역이_동일한경우_경로_조회시_오류_발생_테스트() {
        // when
        var 경로_조회_응답값 = 경로를_조회한다(시청역_id, 시청역_id);

        // then
        응답_상태값이_올바른지_검증한다(경로_조회_응답값, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 지하철 노선 및 구간들이 등록되어 있고
     * When: 출발역과 도착역이 연결이 되어 있지 않은 경로를 조회하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 출발역과_도착역이_연결되지_않은_경로_조회시_오류_발생_테스트() {
        // given
        Long 선릉역_id = 역_추출기.단일_id_를_추출한다(선릉역을_생성한다());
        Long 한티역_id = 역_추출기.단일_id_를_추출한다(한티역을_생성한다());
        분당선을_생성한다(선릉역_id, 한티역_id, 10L);

        // when
        var 경로_조회_응답값 = 경로를_조회한다(시청역_id, 선릉역_id);

        // then
        응답_상태값이_올바른지_검증한다(경로_조회_응답값, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 지하철 노선 및 구간들이 등록되어 있고
     * When: 두 역의 경로를 조회하면
     * Then: 경로에 있는 모든 역의 목록 및 경로의 최단거리를 반환한다.
     */
    @Test
    void 경로_조회_테스트() {
        // when
        var 경로_조회_응답값 = 경로를_조회한다(시청역_id, 충무로역_id);

        // then
        응답_상태값이_올바른지_검증한다(경로_조회_응답값, HttpStatus.OK.value());

        // then
        List<String> 모든_역_이름 = 경로_추출기.경로의_역_이름들을_추출한다(경로_조회_응답값);
        역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(모든_역_이름, "시청역" , "을지로입구역" , "을지로3가역" , "충무로역");

        // then
        Long 거리 = 경로_추출기.경로의_거리를_추출한다(경로_조회_응답값);
        거리가_최소값인지_검증한다(거리, 30L);
    }
}