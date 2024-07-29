package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.LineRequest;

@DisplayName("지하철 구간 관련 기능 인수테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SectionAcceptanceTest {

    @Nested
    @DisplayName("구간 추가")
    class AddSection {
        @Test
        @DisplayName("지하철 노선에 구간을 정상적으로 등록한다")
        void testAddSectionSuccessfully() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, yeoksamStationId, seolleungStationId, 8);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId);
        }

        @Test
        @DisplayName("기존 구간의 상행역 뒤에 신규 구간을 한 개 추가한 뒤 하행역 앞에 신규 구간을 하나 더 추가한다")
        void testAddSectionAfterUpStationAndBeforeDownStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, samsungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, gangnamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = addSection(lineId, yeoksamStationId, seolleungStationId, 7);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("기존 구간의 하행역 앞에 신규 구간을 추가한 뒤 상행역 뒤에 신규 구간을 하나 더 추가한다")
        void testAddSectionBeforeDownStationAndAfterUpStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, samsungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, yeoksamStationId, samsungStationId, 8);

            getLine(lineId);

            // when
            ExtractableResponse<Response> response = addSection(lineId, yeoksamStationId, seolleungStationId, 7);

            getLine(lineId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("기존 구간의 상행역 뒤에 신규 구간을 두 개 추가한다")
        void testAddSectionAfterUpStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, samsungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, gangnamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = addSection(lineId, gangnamStationId, yeoksamStationId, 7);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("존재하지 않는 상행역으로 구간을 추가하려고 하면 실패한다")
        void testAddSectionWithNonExistentUpStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, 999L, seolleungStationId, 8);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("상행역 기준으로 구간 추가 시 기존 구간 거리보다 큰 거리값을 요청하면 실패한다")
        void testAddSectionWithShorterDistanceThanExistingUpStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, seolleungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, gangnamStationId, yeoksamStationId, 15);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("기존 구간의 하행역 전에 신규 구간을 두 개 추가한다")
        void testAddSectionBeforeDownStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, samsungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, yeoksamStationId, samsungStationId, 8);

            // when
            ExtractableResponse<Response> response = addSection(lineId, seolleungStationId, samsungStationId, 7);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("존재하지 않는 하행역으로 구간을 추가하려고 하면 실패한다")
        void testAddSectionWithNonExistentDownStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, yeoksamStationId, 999L, 8);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("하행역 기준으로 구간 추가 시 기존 구간 거리보다 큰 거리값을 요청하면 실패한다")
        void testAddSectionWithShorterDistanceThanExistingDownStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, seolleungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, yeoksamStationId, seolleungStationId, 15);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("구간 추가 후 마지막 구간에 다시 구간을 추가한다")
        void testAddSectionToEndOfLine() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, yeoksamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = addSection(lineId, seolleungStationId, samsungStationId, 7);

            // then
            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("구간 추가 후 첫 구간 앞에 다시 구간을 추가한다")
        void testAddSectionToStartOfLine() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", yeoksamStationId, seolleungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            addSection(lineId, seolleungStationId, samsungStationId, 8);

            // when
            ExtractableResponse<Response> response = addSection(lineId, gangnamStationId, yeoksamStationId, 7);

            // then
            ExtractableResponse<Response> lineResponse = getLine(lineId);
            List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(stationIds).containsExactly(gangnamStationId, yeoksamStationId, seolleungStationId, samsungStationId);
        }

        @Test
        @DisplayName("상행역과 하행역이 모두 기존 구간에 존재하면 추가 실패한다")
        void testAddSectionWithBothExistingStations() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, seolleungStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, gangnamStationId, seolleungStationId, 5);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @Test
        @DisplayName("구간이 이미 존재하는 노선에 구간을 추가하려고 할 때 상행역과 하행역이 모두 기존 구간과 다른 경우 추가를 실패한다")
        void testAddSectionWithBothNonExistingStations() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");
            Long samsungStationId = createStationAndGetId("삼성역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = addSection(lineId, samsungStationId, seolleungStationId, 5);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    @Nested
    @DisplayName("구간 제거")
    class RemoveSection {
        @DisplayName("지하철 노선의 첫 구간을 제거한다")
        @Test
        void testRemoveFirstSection() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");
            addSection(lineId, yeoksamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = removeSection(lineId, gangnamStationId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        /**
         * Given 지하철 노선을 생성하고
         * And 두 지하철역을 생성하고
         * And 새로운 구간을 등록하고
         * When 지하철 노선의 마지막 역을 제거하면
         * Then 역이 제거된다
         */
        @DisplayName("지하철 노선의 마지막 구간을 제거한다")
        @Test
        void testRemoveLastSection() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");
            addSection(lineId, yeoksamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = removeSection(lineId, seolleungStationId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("지하철 노선의 가운데 역으로 구간을 제거한다")
        @Test
        void testRemoveSection() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            Long seolleungStationId = createStationAndGetId("선릉역");

            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");
            addSection(lineId, yeoksamStationId, seolleungStationId, 8);

            // when
            ExtractableResponse<Response> response = removeSection(lineId, yeoksamStationId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        /**
         * Given 지하철 노선을 생성하고
         * And 두 지하철역을 생성하고
         * When 지하철 노선의 구간이 하나만 존재할 때 구간을 제거하면
         * Then 구간 제거가 실패한다
         */
        @DisplayName("구간이 하나만 존재할 때 구간 제거가 실패한다")
        @Test
        void testRemoveSectionWhenOnlyOneExists() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = removeSection(lineId, yeoksamStationId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        /**
         * Given 지하철 노선을 생성하고
         * And 두 지하철역을 생성하고
         * And 새로운 구간을 등록하고
         * When 없는 상행역을 요청하여 구간을 삭제하면
         * Then 구간 삭제가 실패한다
         */
        @DisplayName("없는 역을 상행역을 요청하여 구간 삭제 실패")
        @Test
        void testRemoveSectionWithNonExistentUpStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = removeSection(lineId, 999L);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        /**
         * Given 지하철 노선을 생성하고
         * And 두 지하철역을 생성하고
         * And 새로운 구간을 등록하고
         * When 없는 하행역을 요청하여 구간을 삭제하면
         * Then 구간 삭제가 실패한다
         */
        @DisplayName("없는 역을 하행역을 요청하여 구간 삭제 실패")
        @Test
        void testRemoveSectionWithNonExistentDownStation() {
            // given
            Long gangnamStationId = createStationAndGetId("강남역");
            Long yeoksamStationId = createStationAndGetId("역삼역");
            ExtractableResponse<Response> createLineResponse = createLine(new LineRequest("2호선", "bg-red-600", gangnamStationId, yeoksamStationId, 10));
            Long lineId = createLineResponse.jsonPath().getLong("id");

            // when
            ExtractableResponse<Response> response = removeSection(lineId, 1000L);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
