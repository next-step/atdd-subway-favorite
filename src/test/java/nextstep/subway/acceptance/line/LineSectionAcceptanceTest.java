package nextstep.subway.acceptance.line;

import static nextstep.Fixtures.*;
import static nextstep.subway.acceptance.line.steps.LineSectionAcceptanceSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 구간 관리 기능 인수테스트")
class LineSectionAcceptanceTest {
  private Station 강남역;
  private Station 역삼역;
  private Station 선릉역;
  private Station 판교역;
  private Line 이호선;

  @Nested
  @DisplayName("지하철 구간 추가 인수테스트")
  class AddLineSectionAcceptanceTest extends AcceptanceTest {
    @Autowired private StationRepository stationRepository;
    @Autowired private LineRepository lineRepository;

    /** Given 지하철역들을 생성 하고 */
    @Override
    @BeforeEach
    protected void setUp() {
      super.setUp();
      강남역 = stationRepository.save(강남역());
      역삼역 = stationRepository.save(역삼역());
      선릉역 = stationRepository.save(선릉역());
      판교역 = stationRepository.save(판교역());
    }

    /** Given 구간의 상행역이 등록되어 있지 않고 하행역이 노선의 상행 종점역과 같은 경우 When 구간 등록을 하면 Then 노선 조회시 해당 구간이 첫 구간이다 */
    @DisplayName("구간이 노선 첫 구간으로 등록된다.")
    @Test
    void shouldPrependLineSection() {
      LineSections 역삼_선릉_구간 = LineSections.of(역삼역, 선릉역, 10);
      이호선 = lineRepository.save(aLine().lineSections(역삼_선릉_구간).build());
      LineSection 강남_역삼_구간 = LineSection.of(강남역, 역삼역, 20);

      ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 강남_역삼_구간);

      노선_첫_구간으로_등록됨(response, 이호선, 강남_역삼_구간);
    }

    /**
     * Given 구간의 하행역이 등록되어 있지 않고 상행역이 노선의 하행 종점역과 같은 경우 When 구간 등록을 하면 Then 노선 조회시 해당 구간이 마지막 구간이다
     */
    @DisplayName("구간이 노선 마지막 구간으로 등록된다.")
    @Test
    void shouldAppendLineSection() {
      LineSections 강남_역삼_구간 = LineSections.of(강남역, 역삼역, 10);
      이호선 = lineRepository.save(aLine().lineSections(강남_역삼_구간).build());
      LineSection 역삼_선릉_구간 = LineSection.of(역삼역, 선릉역, 20);

      ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 역삼_선릉_구간);

      노선_마지막_구간으로_등록됨(response, 이호선, 역삼_선릉_구간);
    }

    /** Given 구간의 하행 역이 이미 해당 노선에 등록되어 있으면 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
    @DisplayName("종점 하행역과 같은 상행역인 구간 등록 시 구간의 하행 역이 이미 해당 노선에 등록되어 있으면 에러가 발생한다.")
    @Test
    void appendLineSectionCycle() {
      이호선 = lineRepository.save(aLine().lineSections(new LineSections(강남_역삼_구간())).build());
      LineSection cyclicSection =
          LineSection.builder().upStation(역삼역).downStation(강남역).distance(20).build();

      ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, cyclicSection);

      노선_구간_요청_실패함(response);
    }

    /** Given 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아니고 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
    @DisplayName("구간이 노선 중간에 등록된다.")
    @Test
    void shouldAddLineSectionToMiddle() {
      LineSections 강남_선릉_구간 = LineSections.of(강남역, 선릉역, 30);
      이호선 = lineRepository.save(aLine().lineSections(강남_선릉_구간).build());
      LineSection 강남_역삼_구간 = LineSection.of(강남역, 역삼역, 10);

      ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 강남_역삼_구간);

      노선_i변째_구간으로_등록됨(response, 이호선, 강남_역삼_구간, 1);
    }

    /** Given 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아니고 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
    @DisplayName("노선과 겹치는 역이 없는 구간을 등록 시 에러가 발생한다.")
    @Test
    void addDisjointLineSectionReturnsBadRequest() {
      LineSections 강남_역삼_구간 = LineSections.of(강남역, 역삼역, 10);
      이호선 = lineRepository.save(aLine().lineSections(강남_역삼_구간).build());
      LineSection disjointedSection =
          LineSection.builder().upStation(선릉역).downStation(판교역).distance(20).build();

      ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, disjointedSection);

      노선_구간_요청_실패함(response);
    }
  }

  @Nested
  @DisplayName("지하철 구간 제거 인수테스트")
  class RemoveLineSectionAcceptanceTest extends AcceptanceTest {
    @Autowired StationRepository stationRepository;
    @Autowired LineRepository lineRepository;

    /** Given 지하철역과 노선 그리고 하나 이상의 구간이 등록되어 있고 */
    @Override
    @BeforeEach
    protected void setUp() {
      super.setUp();
      강남역 = stationRepository.save(강남역());
      역삼역 = stationRepository.save(역삼역());
      선릉역 = stationRepository.save(선릉역());
      판교역 = stationRepository.save(판교역());
      이호선 =
          lineRepository.save(
              aLine().lineSections(new LineSections(강남_역삼_구간(), 역삼_선릉_구간())).build());
    }

    /** When 하행 종점역 구간 제거를 요청하면 Then 해당 노선 조회 시 역 목록에서 하행 종점이 제거된다. */
    @DisplayName("하행 종점역을 제거한다.")
    @Test
    void shouldRemoveTerminalDownStation() {
      ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 선릉역);
      노선_구간_삭제됨(response, 이호선, 선릉역);
    }

    /** When 상행 종점역 구간 제거를 요청하면 Then 해당 노선 조회 시 역 목록에서 상행 종점역이 제거된다. */
    @DisplayName("상행 종점역을 제거한다.")
    @Test
    void shouldRemoveTerminalUpStation() {
      ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 강남역);
      노선_구간_삭제됨(response, 이호선, 강남역);
    }

    /** When 노선 중간역 구간 제거를 요청하면 Then 해당 노선 조회 시 역 목록에서 중간역이 제거된다. */
    @DisplayName("노선 가운데 역을 제거한다.")
    @Test
    void shouldRemoveStationInTheMiddle() {
      ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 역삼역);
      노선_구간_삭제됨(response, 이호선, 역삼역);
    }

    @DisplayName("노선 구간에 등록되어 있지 않은 역을 제거 요청하면 404 Not Found 에러가 응답된다.")
    @Test
    void shouldReturnErrorWhenStationDoesNotExist() {
      ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 판교역);
      노선_구간_삭제_실패함(response, HttpStatus.NOT_FOUND);
    }

    /** Given 노선의 구간이 하나인 경우 When 구간을 삭제 요청하면 Then 400 Bad Request 에러가 응답된다. */
    @DisplayName("구간이 하나만 존재할 때 구간 제거 시 에러가 발생한다.")
    @Test
    void removeLastLineSectionShouldReturnError() {
      노선_구간_삭제_요청(이호선, 선릉역);

      ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 역삼역);

      노선_구간_삭제_실패함(response, HttpStatus.BAD_REQUEST);
    }
  }
}
