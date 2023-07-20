package subway.acceptance.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.acceptance.station.StationFixture;
import subway.utils.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.station.StationFixture.getStationId;

@DisplayName("지하철노선 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void addLine() {
        // "교대역", "강남역", "역삼역", "선릉역", "삼성역", "잠실역", "강변역", "건대역", "성수역", "왕십리역"
        StationFixture.기본_역_생성_호출();
    }

    // Week 1

    /**
     * When 노선을 생성하면
     * Then 상행역과 하행역이 포함된 기본 구간이 생성되고
     * Then 노선 조회로 상행역과 하행역이 포함된 기본 구간이 있다.
     */
    @DisplayName("기본 구간을 생성 한다.")
    @Test
    void createSection() {
        // when
        var 이호선_요청 = LineFixture.이호선_요청_만들기(getStationId("강남역"), getStationId("역삼역"));
        var createLineResponse = LineSteps.노선_생성_API(이호선_요청);
        var createdLocation = createLineResponse.header("Location");

        // then
        assertThat(createLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var retrieveLinesResponse = LineSteps.노선_조회_API(createdLocation);
        List<String> stationNames = retrieveLinesResponse.jsonPath().getList("stations.name", String.class);
        assertThat(stationNames.size()).isEqualTo(2);

    }

    /**
     * When 기본 노선의 구간이 있을 때
     * Then 노선의 하행역을 새로운 구간의 상행역으로 지정한 구간을 추가 한다.
     */
    @DisplayName("노선의 구간에 새로운 구간을 추가 한다.")
    @Test
    void appendStationToSection() {
        // when
        var 이호선_요청 = LineFixture.이호선_요청_만들기(getStationId("강남역"), getStationId("역삼역"));
        var createLineResponse = LineSteps.노선_생성_API(이호선_요청);
        var createdLocation = createLineResponse.header("Location");

        // then
        var 구간_요청 = SectionFixture.구간_요청_만들기(getStationId("역삼역"), getStationId("선릉역"), 10L);
        var createSectionResponse = LineSteps.구간_추가_API(createdLocation, 구간_요청);
        LineSteps.노선_조회_API(createdLocation);

        assertThat(createSectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Given 노선이 있고
     * When 노선의 역이 2개 이하 일 때
     * When 노선의 하행역을 제거하면
     * Then 역이 제거되지 않는다.
     */
    @DisplayName("노선의 구간이 1개 뿐일때 역을 제거할 수 없다.")
    @Test
    void deleteStationFromMinimalSection() {
        // given
        String createdLocation = 세구간이_포함된_노선_생성_작업();
        var appendLocation = createdLocation + "/sections";

        // when
        var stationId = getStationId("선릉역");
        LineSteps.구간_제거_API(appendLocation, stationId);

        // when
        var additionalDeleteLocation = getStationId("역삼역");
        var deleteSectionResponse = LineSteps.구간_제거_API(appendLocation, additionalDeleteLocation);

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Week 2

    /**
     * Given 노선이 있고
     * When 새로운 구간의 하행역을 기존 노선의 상행역으로 지정하면
     * Then 노선이 추가된다
     */
    @DisplayName("노선의 가장 앞에 새 구간을 추가한다.")
    @Test
    void appendSectionBeforeLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 상행에_추가하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("교대역"), getStationId("강남역"), 10L);
        var response = LineSteps.구간_추가_API(createdLocation, 상행에_추가하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineSteps.노선_조회_API(createdLocation);

    }

    /**
     * Given 노선이 있고
     * When 새로운 구간의 상행역을 기존 노선의 하행역으로 지정하면
     * Then 노선이 추가된다
     */
    @DisplayName("노선의 가장 뒤에 새 구간을 추가한다.")
    @Test
    void appendSectionBehindLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 하행에_추가하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("선릉역"), getStationId("삼성역"), 10L);
        var response = LineSteps.구간_추가_API(createdLocation, 하행에_추가하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineSteps.노선_조회_API(createdLocation);
    }

    /**
     * Given 노선이 있고
     * When 새로운 구간의 상행역과 기존 노선의 역이 같고 새로운 구간의 하행역은 노선에 없으면
     * Then 노선이 추가된다
     */
    @DisplayName("노선의 중간에 상행역이 같은 새 구간을 추가한다.")
    @Test
    void appendSectionNewUpStationExistUpStationIsInMiddleOfLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 노선_중간에_추가하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("역삼역"), getStationId("왕십리역"), 5L);
        var response = LineSteps.구간_추가_API(createdLocation, 노선_중간에_추가하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineSteps.노선_조회_API(createdLocation);
    }

    /**
     * Given 노선이 있고
     * When 새로운 구간의 하행역과 기존 노선의 역이 같고 새로운 구간의 상행역은 노선에 없으면
     * Then 노선이 추가된다
     */
    @DisplayName("노선의 중간에 하행역이 같은 새 구간을 추가한다.")
    @Test
    void appendSectionNewDownStationExistDownStationIsInMiddleOfLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 노선_중간에_추가하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("왕십리역"), getStationId("역삼역"), 5L);
        var response = LineSteps.구간_추가_API(createdLocation, 노선_중간에_추가하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineSteps.노선_조회_API(createdLocation);
    }

    /**
     * Given 노선이 있고
     * When 기존 구간 사이에 구간을 추가 할 때
     * When 새로운 구간의 길이가 기존 구간의 길이 이상이면
     * Then 노선에 추가되지 않는다.
     */
    @DisplayName("노선의 길이를 넘는 구간은 추가할 수 없다.")
    @Test
    void appendSectionOverLineDistance() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 노선_중간에_추가하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("왕십리역"), getStationId("역삼역"), 11L);
        var response = LineSteps.구간_추가_API(createdLocation, 노선_중간에_추가하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 노선이 있고
     * When 기존 구간 사이에 구간을 추가 할 때
     * When 상행역과 하행역이 모두 노선의 구간에 존재하면
     * Then 노선에 추가되지 않는다.
     */
    @DisplayName("상행역과 하행역 모두 노선에 이미 존재하는 구간은 추가 할 수 없다.")
    @Test
    void appendSectionWithBothUpStationAndDownStationAlreadyExistInLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 이미_존재하는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("역삼역"), getStationId("삼성역"), 10L);
        var response = LineSteps.구간_추가_API(createdLocation, 이미_존재하는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 노선이 있고
     * When 새로운 구간의 상행역과 하행역이 모두 기존 노선에 존재하지 않으면
     * Then 노선에 추가되지 않는다.
     * 상행역과 하행역이 모두 노선에 존재하지 않는 구간의 추가
     */
    @DisplayName("상행역과 하행역 모두가 노선에 존재하지 않는 구간은 추가 할 수 없다.")
    @Test
    void appendSectionWithNeitherUpStationNorDownStationExistInLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();

        // when
        var 존재하지_않는_구간_요청 = SectionFixture.구간_요청_만들기(getStationId("왕십리역"), getStationId("성수역"), 10L);
        var response = LineSteps.구간_추가_API(createdLocation, 존재하지_않는_구간_요청);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    // week 2-2

    /**
     * Given 구간이 3개인 노선이 있고
     * When 중간 구간을 삭제 하면
     * Then 중간 구간이 삭제 된다.
     */
    @DisplayName("노선의 중간 구간을 제거한다.")
    @Test
    void deleteStationInMiddleOfSection() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();
        var appendLocation = createdLocation + "/sections";
        LineSteps.노선_조회_API(createdLocation);

        // when
        var stationId = getStationId("역삼역");
        var deleteSectionResponse = LineSteps.구간_제거_API(appendLocation, stationId);

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        LineSteps.노선_조회_API(createdLocation);
    }

    /**
     * Given 3개의 역이 등록된 구간을 가진 노선이 있고
     * When 노선의 가장 앞의 구간을 제거하면
     * Then 구간이 삭제되고
     * Then 2개의 역을 가진 노선이 된다
     */
    @DisplayName("노선의 가장 앞 구간을 삭제한다.")
    @Test
    void deleteSectionAtFrontOfLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();
        var appendLocation = createdLocation + "/sections";

        // when
        var deleteSectionResponse = LineSteps.구간_제거_API(appendLocation, getStationId("강남역"));

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        var lineRetrieveResponse = LineSteps.노선_조회_API(createdLocation);
        List<String> stations = lineRetrieveResponse.jsonPath().getList("stations.name", String.class);
        assertThat(stations).containsExactlyInAnyOrder("역삼역", "선릉역");
    }

    /**
     * Given 3개의 역이 등록된 구간을 가진 노선이 있고
     * When 노선의 가장 뒤의 구간을 제거하면
     * Then 구간이 삭제되고
     * Then 2개의 역을 가진 노선이 된다
     */
    @DisplayName("노선의 가장 뒤 구간을 삭제한다.")
    @Test
    void deleteSectionAtEndOfLine() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();
        var appendLocation = createdLocation + "/sections";

        // when
        var deleteSectionResponse = LineSteps.구간_제거_API(appendLocation, getStationId("선릉역"));

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        var lineRetrieveResponse = LineSteps.노선_조회_API(createdLocation);
        var stations = lineRetrieveResponse.jsonPath().getList("stations.name", String.class);
        assertThat(stations.size()).isEqualTo(2);
    }

    /**
     * Given 구간이 3개인 노선이 있고
     * When 노선에 존재하지 않는 역을 제거하면
     * Then 구간이 삭제되지 않는다.
     */
    @DisplayName("노선에 존재하지 않는 역은 제거되지 않는다.")
    @Test
    void deleteNotExistStationInMiddleOfSection() {
        // given
        var createdLocation = 세구간이_포함된_노선_생성_작업();
        var appendLocation = createdLocation + "/sections";
        LineSteps.노선_조회_API(createdLocation);

        // when
        var stationId = getStationId("강변역");
        var deleteSectionResponse = LineSteps.구간_제거_API(appendLocation, stationId);

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    private String 세구간이_포함된_노선_생성_작업() {
        var 이호선_요청 = LineFixture.이호선_요청_만들기(getStationId("강남역"), getStationId("역삼역"));
        var 노선_생성 = LineSteps.노선_생성_API(이호선_요청);
        var createdLocation = 노선_생성.header("Location");

        var 구간_요청 = SectionFixture.구간_요청_만들기(getStationId("역삼역"), getStationId("선릉역"), 10L);
        LineSteps.구간_추가_API(createdLocation, 구간_요청);

        return createdLocation;
    }
}

