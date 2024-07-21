package nextstep.subway.acceptance;

import nextstep.subway.setup.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.step.BaseStepAsserter.응답_상태값이_올바른지_검증한다;
import static nextstep.subway.acceptance.step.StationStep.*;
import static nextstep.subway.acceptance.step.StationStepAsserter.*;
import static nextstep.subway.acceptance.step.StationStepExtractor.역_추출기;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends BaseTestSetup {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @ParameterizedTest
    @ValueSource(strings = {"강남역", "역삼역", "삼성역"})
    void 새로운_역_생성_테스트(String 역이름) {
        // when
        var 역생성_응답값 = 역을_생성한다(역이름);

        // then
        응답_상태값이_올바른지_검증한다(역생성_응답값, HttpStatus.CREATED.value());

        // then
        List<String> 역_이름_목록 = 역_추출기.모든_역의_이름을_추출한다(역_목록을_조회한다());
        역_목록에_지정된_역이_포함되는지_검증한다(역_이름_목록, 역이름);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @ParameterizedTest
    @CsvSource({"강남역, 잠실역", "역삼역, 삼성역", "삼성역, 선릉역"})
    void 역_목록_조회_테스트(String 첫번째역, String 두번째역) {
        // given
        역을_생성한다(첫번째역);
        역을_생성한다(두번째역);

        // when
        var 역_목록조회_응답값 = 역_목록을_조회한다();

        // then
        응답_상태값이_올바른지_검증한다(역_목록조회_응답값, HttpStatus.OK.value());

        // then
        List<String> 역_이름_목록 = 역_추출기.모든_역의_이름을_추출한다(역_목록조회_응답값);
        역_목록의_크기를_검증한다(역_이름_목록, 2);
        역_목록에_지정된_역들이_순서대로_포함되는지_검증한다(역_이름_목록, 첫번째역, 두번째역);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @ParameterizedTest
    @ValueSource(strings = {"강남역", "역삼역", "삼성역"})
    void 역_삭제_테스트(String 역이름) {
        // given
        long 역_id = 역_추출기.단일_id_를_추출한다(역을_생성한다(역이름));

        // when
        var 역삭제_응답값 = 역을_삭제한다(역_id);

        // then
        응답_상태값이_올바른지_검증한다(역삭제_응답값, HttpStatus.NO_CONTENT.value());

        // then
        List<String> 역_이름_목록 = 역_추출기.모든_역의_이름을_추출한다(역_목록을_조회한다());
        역_목록에_지정된_역이_포함되지_않는지_검증한다(역_이름_목록, 역이름);
    }
}