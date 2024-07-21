package nextstep.subway.acceptance;

import nextstep.subway.setup.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.fixtures.LineAcceptanceFixture.이호선_생성_요청값을_생성한다;
import static nextstep.subway.acceptance.fixtures.LineAcceptanceFixture.일호선_생성_요청값을_생성한다;
import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;
import static nextstep.subway.acceptance.step.LineStep.*;
import static nextstep.subway.acceptance.step.LineStepAsserter.*;
import static nextstep.subway.acceptance.step.LineStepExtractor.노선_추출기;
import static nextstep.subway.acceptance.step.StationStep.*;
import static nextstep.subway.acceptance.step.StationStepAsserter.역_목록에_지정된_역들이_포함되는지_검증한다;
import static nextstep.subway.acceptance.step.StationStepExtractor.역_추출기;

@DisplayName("지하철 노선 관련 인수 테스트")
public class LineAcceptanceTest extends BaseTestSetup {
    /**
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @Test
    void 노선_생성_테스트() {
        // given
        var 알호선_생성_요청값 = 일호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(서울역을_생성한다()),
                역_추출기.단일_id_를_추출한다(시청역을_생성한다())
        );

        // when
        var 노선생성_응답값 = 노선을_생성한다(알호선_생성_요청값);

        // then
        응답_상태값이_올바른지_검증한다(노선생성_응답값, HttpStatus.CREATED.value());

        // then
        List<String> 모든_노선명 = 노선_추출기.모든_노선명_목록을_추출한다(노선_목록을_조회한다());
        노선_목록에_지정된_노선이_포함되는지_검증한다(모든_노선명, "1호선");
    }

    /**
     * Given: 2개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 2개의 지하철 노선 목록이 반환된다.
     */
    @Test
    void 노선_목록_조회_테스트() {
        // given
        노선을_생성한다(일호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(서울역을_생성한다()),
                역_추출기.단일_id_를_추출한다(시청역을_생성한다())
        ));

        노선을_생성한다(이호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(역삼역을_생성한다()),
                역_추출기.단일_id_를_추출한다(잠실역을_생성한다())
        ));

        // when
        var 노선목록조회_응답값 = 노선_목록을_조회한다();

        // then
        응답_상태값이_올바른지_검증한다(노선목록조회_응답값, HttpStatus.OK.value());

        // then
        List<String> 모든_노선명 = 노선_추출기.모든_노선명_목록을_추출한다(노선목록조회_응답값);
        노선_목록에_지정된_노선들이_포함되는지_검증한다(모든_노선명, "1호선", "2호선");

        List<String> 일호선_역_이름_목록 = 노선_추출기.모든_노선에_포함된_역_이름을_추출한다(노선목록조회_응답값, "1호선");
        역_목록에_지정된_역들이_포함되는지_검증한다(일호선_역_이름_목록, "서울역", "시청역");

        List<String> 이호선_역_이름_목록 = 노선_추출기.모든_노선에_포함된_역_이름을_추출한다(노선목록조회_응답값, "2호선");
        역_목록에_지정된_역들이_포함되는지_검증한다(이호선_역_이름_목록, "역삼역", "잠실역");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @Test
    void 노선_조회_테스트() {
        // given
        var 일호선_생성_응답값 = 노선을_생성한다(일호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(서울역을_생성한다()),
                역_추출기.단일_id_를_추출한다(시청역을_생성한다())
        ));
        Long id = 노선_추출기.단일_id_를_추출한다(일호선_생성_응답값);

        // when
        var 노선조회_응답값 = 노선을_조회한다(id);

        // then
        응답_상태값이_올바른지_검증한다(노선조회_응답값, HttpStatus.OK.value());

        // then
        노선_이름이_일치하는지_검증한다(노선_추출기.단일_노선명을_추출한다(노선조회_응답값), "1호선");

        List<String> 일호선_역_이름_목록 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선조회_응답값);
        역_목록에_지정된_역들이_포함되는지_검증한다(일호선_역_이름_목록, "서울역", "시청역");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @Test
    void 노선_수정_테스트() {
        // given
        var 일호선_생성_응답값 = 노선을_생성한다(일호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(서울역을_생성한다()),
                역_추출기.단일_id_를_추출한다(시청역을_생성한다())
        ));
        Long 노선_id = 노선_추출기.단일_id_를_추출한다(일호선_생성_응답값);

        // when
        var 노선수정_응답값 = 노선을_수정한다(노선_id, "2호선", "#00A84D");

        // then
        응답_상태값이_올바른지_검증한다(노선수정_응답값, HttpStatus.OK.value());

        // then
        var 노선조회_응답값 = 노선을_조회한다(노선_id);
        노선_이름이_일치하는지_검증한다(노선_추출기.단일_노선명을_추출한다(노선조회_응답값), "2호선");
        노선_색상이_일치하는지_검증한다(노선_추출기.단일_색상을_추출한다(노선조회_응답값), "#00A84D");
    }

    /**
     * 지하철 노선 삭제
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @Test
    void 노선_삭제_테스트() {
        // given
        var 일호선_생성_응답값 = 노선을_생성한다(일호선_생성_요청값을_생성한다(
                역_추출기.단일_id_를_추출한다(서울역을_생성한다()),
                역_추출기.단일_id_를_추출한다(시청역을_생성한다())
        ));
        Long id = 노선_추출기.단일_id_를_추출한다(일호선_생성_응답값);

        // when
        var 노선삭제_응답값 = 노선을_삭제한다(id);

        // then
        응답_상태값이_올바른지_검증한다(노선삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        List<String> 모든_노선명 = 노선_추출기.모든_노선명_목록을_추출한다(노선_목록을_조회한다());
        노선_목록에_지정된_노선이_포함되지_않는지_검증한다(모든_노선명, "1호선");
    }
}
