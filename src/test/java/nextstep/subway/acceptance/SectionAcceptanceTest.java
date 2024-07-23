package nextstep.subway.acceptance;

import nextstep.subway.setup.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.fixtures.LineAcceptanceFixture.일호선_생성_요청값을_생성한다;
import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;
import static nextstep.subway.acceptance.step.LineStep.*;
import static nextstep.subway.acceptance.step.LineStepExtractor.노선_추출기;
import static nextstep.subway.acceptance.step.StationStep.*;
import static nextstep.subway.acceptance.step.StationStepAsserter.역_목록에_지정된_역들이_순서대로_포함되는지_검증한다;
import static nextstep.subway.acceptance.step.StationStepAsserter.역_목록에_지정된_역이_포함되지_않는지_검증한다;
import static nextstep.subway.acceptance.step.StationStepExtractor.역_추출기;

@DisplayName("지하철 구간 관련 인수 테스트")
public class SectionAcceptanceTest extends BaseTestSetup {
    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역과 하행역이 노선에 존재하지 않도록 구간을 추가하면
     * Then: 상행역이 잘못되었다는 오류가 발생한다.
     */
    @Test
    void 존재하지_않는_구간_추가시_오류_발생_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        Long 서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());
        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());

        // when
        var 구간_추가_응답값 = 구간을_추가한다(일호선_id, 서울역_id, 구로역_id, 10L);

        // then
        응답_상태값이_올바른지_검증한다(구간_추가_응답값, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 하행역이 노선에 이미 포함된 역이라면
     * Then: 하행역이 잘못되었다는 오류가 발생한다.
     */
    @Test
    void 하행역이_잘못된_경우_구간_추가시_오류_발생_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        // when
        var 구간_추가_응답값 = 구간을_추가한다(일호선_id, 용산역_id, 시청역_id, 10L);

        // then
        응답_상태값이_올바른지_검증한다(구간_추가_응답값, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 구간의 상행역이 노선의 하행종창역이 되도록 구간을 추가하면
     * Then: 해당 지하철 구간이 추가된다.
     */
    @Test
    void 하행종착역_구간_추가_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());

        // when
        var 구간_추가_응답값 = 구간을_추가한다(일호선_id, 용산역_id, 구로역_id, 10L);

        // then
        응답_상태값이_올바른지_검증한다(구간_추가_응답값, HttpStatus.CREATED.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(모든_역_이름, "시청역", "용산역", "구로역");
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 노선의 상행종착역이 되도록 구간을 추가하면
     * Then: 해당 지하철 구간이 추가된다.
     */
    @Test
    void 상행종착역_구간_추가_테스트() {
        // given
        Long 서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(서울역_id, 용산역_id))
        );

        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());

        // when
        var 구간_추가_응답값 = 구간을_추가한다(일호선_id, 시청역_id, 서울역_id, 10L);

        // then
        응답_상태값이_올바른지_검증한다(구간_추가_응답값, HttpStatus.CREATED.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(모든_역_이름, "시청역", "서울역", "용산역");
    }


    /**
     * Given: 특정 지하철 노선이 등록되어 있고
     * When: 노선의 중간이 되도록 구간을 추가하면
     * Then: 해당 지하철 구간이 추가된다.
     */
    @Test
    void 중간_구간_추가_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 구로역_id))
        );

        Long 서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());

        // when
        구간을_추가한다(일호선_id, 시청역_id, 서울역_id, 3L);
        var 구간_추가_응답값 = 구간을_추가한다(일호선_id, 서울역_id, 용산역_id, 5L);

        // then
        응답_상태값이_올바른지_검증한다(구간_추가_응답값, HttpStatus.CREATED.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(모든_역_이름, "시청역", "서울역", "용산역", "구로역");
    }

    /**
     * Given: 한개의 노선과 한개의 구간이 등록되어 있고
     * When: 노선의 마지막 역을 삭제하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 노선에_구간이_한개뿐인_경우_구간_삭제시_오류_발생_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        // when
        var 구간_삭제_응답값 = 구간을_삭제한다(일호선_id, 용산역_id);

        // then
        응답_상태값이_올바른지_검증한다(구간_삭제_응답값, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 한개의 노선과 두개의 구간이 등록되어 있고
     * When: 노선에 포함된 역이 아닌 역을 삭제하면
     * Then: 오류가 발생한다.
     */
    @Test
    void 삭제할_역이_잘못된_경우_구간_삭제시_오류_발생_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );
        구간을_추가한다(일호선_id, 용산역_id, 구로역_id, 10L);

        Long 서울역_id = 역_추출기.단일_id_를_추출한다(서울역을_생성한다());

        // when
        var 구간삭제_응답값 = 구간을_삭제한다(일호선_id, 서울역_id);

        // then
        응답_상태값이_올바른지_검증한다(구간삭제_응답값, HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given: 한개의 노선과 두개의 구간이 등록되어 있고
     * When: 노선의 처음 역을 삭제하면
     * Then: 해당 역을 상행역으로 하는 구간이 삭제된다.
     */
    @Test
    void 상행종착역_구간_삭제_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());
        구간을_추가한다(일호선_id, 용산역_id, 구로역_id, 10L);

        // when
        var 구간삭제_응답값 = 구간을_삭제한다(일호선_id, 시청역_id);

        // then
        응답_상태값이_올바른지_검증한다(구간삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역이_포함되지_않는지_검증한다(모든_역_이름, "시청역");
    }

    /**
     * Given: 한개의 노선과 두개의 구간이 등록되어 있고
     * When: 노선의 처음 역을 삭제하면
     * Then: 해당 역을 포함하는 구간이 삭제된다.
     */
    @Test
    void 중간_구간_삭제_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());
        구간을_추가한다(일호선_id, 용산역_id, 구로역_id, 10L);

        // when
        var 구간삭제_응답값 = 구간을_삭제한다(일호선_id, 용산역_id);

        // then
        응답_상태값이_올바른지_검증한다(구간삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역이_포함되지_않는지_검증한다(모든_역_이름, "용산역");
    }


    /**
     * Given: 한개의 노선과 두개의 구간이 등록되어 있고
     * When: 노선의 마지막 역을 삭제하면
     * Then: 해당 역을 하행역으로 하는 구간이 삭제된다.
     */
    @Test
    void 하행종착역_구간_삭제_테스트() {
        // given
        Long 시청역_id = 역_추출기.단일_id_를_추출한다(시청역을_생성한다());
        Long 용산역_id = 역_추출기.단일_id_를_추출한다(용산역을_생성한다());
        Long 일호선_id = 노선_추출기.단일_id_를_추출한다(
                노선을_생성한다(일호선_생성_요청값을_생성한다(시청역_id, 용산역_id))
        );

        Long 구로역_id = 역_추출기.단일_id_를_추출한다(구로역을_생성한다());
        구간을_추가한다(일호선_id, 용산역_id, 구로역_id, 10L);

        // when
        var 구간삭제_응답값 = 구간을_삭제한다(일호선_id, 구로역_id);

        // then
        응답_상태값이_올바른지_검증한다(구간삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        List<String> 모든_역_이름 = 노선_추출기.단일_노선에_포함된_역_이름을_추출한다(노선을_조회한다(일호선_id));
        역_목록에_지정된_역이_포함되지_않는지_검증한다(모든_역_이름, "구로역");
    }
}
