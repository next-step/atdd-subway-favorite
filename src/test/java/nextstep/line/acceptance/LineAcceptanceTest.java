package nextstep.line.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import static nextstep.line.acceptance.LineApiRequest.*;
import static nextstep.station.acceptance.StationApiRequest.역을_생성한다;
import static nextstep.utils.HttpStatusAssertion.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 선릉역;
    private Long 삼성역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 역을_생성한다("강남역").jsonPath().getLong("id");
        선릉역 = 역을_생성한다("선릉역").jsonPath().getLong("id");
        삼성역 = 역을_생성한다("삼성역").jsonPath().getLong("id");
    }

    @Nested
    class WhenNew {

        @DisplayName("노선 등록시 노선명,노선색,상행역,하행역,거리를 함께 등록하고 생성된 결과를 응답받는다.")
        @Test
        void createLine() {
            // Given 노선을 생성하면

            // Then 신규 노선이 생성된다.
            var 신규노선 = 노선을_생성한다("2호선", "bg-green-600", 강남역, 선릉역, 10L);

            //Then 생성된 노선을 응답받는다.
            assertAll(() -> {
                var jsonPath = 신규노선.jsonPath();
                assertCreated(신규노선.statusCode());
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.name", String.class)).containsAnyOf("강남역", "선릉역");
            });

        }

    }

    @Nested
    class WhenShow {

        @DisplayName("모든 노선과 해당하는 지하철역을 모두 반환한다.")
        @Test
        void showLines() {

            //Given 노선을 생성하고
            노선을_생성한다("2호선", "bg-green-600", 강남역, 선릉역, 10L);
            노선을_생성한다("3호선", "bg-orange-600", 선릉역, 삼성역, 20L);

            //When 노선 목록을 조회하면
            var jsonPath = 노선을_모두_조회한다().jsonPath();

            //Then 생성된 노선 목록이 모두 조회된다.
            assertAll(() -> {
                assertThat(jsonPath.getList("name")).containsAnyOf("2호선", "3호선");
                assertThat(jsonPath.getList("color")).containsAnyOf("bg-green-600", "bg-orange-600");
                assertThat(jsonPath.getList("stations[0].name", String.class)).containsAnyOf("강남역", "선릉역");
                assertThat(jsonPath.getList("stations[1].name", String.class)).containsAnyOf("선릉역", "삼성역");
            });

        }

        @DisplayName("특정 노선에 해당하는 정보와 역을 반환한다.")
        @Test
        void showLine() {
            //Given 노선을 여러개 생성하고
            var response = 노선을_생성한다("2호선", "bg-green-600", 강남역, 선릉역, 10L);
            노선을_생성한다("3호선", "bg-orange-600", 선릉역, 삼성역, 10L);
            노선을_생성한다("4호선", "bg-blue-600", 삼성역, 강남역, 10L);

            //When 한 노선을 조회하면
            var location = response.header(HttpHeaders.LOCATION);
            var 조회_결과 = 노선을_조회한다(location);

            // Then 해당 노선이 조회된다.

            assertAll(() -> {
                assertOk(조회_결과.statusCode());
                var jsonPath = 조회_결과.jsonPath();
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.name", String.class)).containsAnyOf("강남역", "선릉역");
            });
        }

        @DisplayName("조회하려는 노선이 존재하지 않으면 400 상태코드를 반환한다.")
        @Test
        void whenShowNotExistThanReturn400() {
            // When 조회하려는 노선이 존재하지 않으면
            var 조회_결과 = 노선을_조회한다("/lines/0");
            // Then 400 상태코드를 반환한다.
            assertBadRequest(조회_결과.statusCode());
        }

    }


    @Nested
    class WhenUpdate {

        @DisplayName("노선을 수정한후 조회시 수정된 정보가 반환된다.")
        @Test
        void whenUpdateLine() {
            //Given 노선을 생성하고
            var response = 노선을_생성한다("2호선", "bg-green-600", 강남역, 선릉역, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 노선을 수정한 뒤
            var updateResponse = 노선을_업데이트한다(location, "3호선", "bg-orange-500");

            //When 조회하면
            var jsonPath = 노선을_조회한다(location).jsonPath();

            //Then 수정된 결과가 반환된다.
            assertAll(() -> {
                assertNoContent(updateResponse.statusCode());
                assertThat(jsonPath.getString("name")).isEqualTo("3호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-orange-500");
                assertThat(jsonPath.getList("stations.name", String.class)).containsAnyOf("강남역", "선릉역");
            });
        }

        @DisplayName("존재하지 않는 노선을 수정하면 400 상태코드르 반환한다.")
        @Test
        void whenNotExistLineUpdateThenReturn400() {
            //when 존재하지 않는 노선을 수정하면
            var 수정_결과 = 노선을_업데이트한다("/lines/0", "3호선", "bg-orange-500");

            //then 400 상태코드르 반환한다.
            assertBadRequest(수정_결과.statusCode());
        }

    }

    @Nested
    class WhenDelete {

        @DisplayName("삭제하려는 노선이 존재하면 삭제된 뒤 목록에서 제외된다")
        @Test
        void whenDeleteLine() {
            //Given 여러 노선을 생성하고
            var response = 노선을_생성한다("2호선", "bg-green-600", 강남역, 삼성역, 10L);
            노선을_생성한다("3호선", "bg-orange-500", 삼성역, 선릉역, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 그 중 한 노선을 삭제하면
            var 삭제결과 = 노선을_삭제한다(location);

            //Then 삭제한 노선은 목록에서 제외된다
            var jsonPath = 노선을_모두_조회한다().jsonPath();

            assertAll(() -> {
                assertNoContent(삭제결과.statusCode());
                assertThat(jsonPath.getList("name", String.class)).containsOnly("3호선");
                assertThat(jsonPath.getList("color", String.class)).containsOnly("bg-orange-500");
            });
        }

        @DisplayName("삭제하려는 노선이 존재하지 않으면 응답코드 400을 반환한다")
        @Test
        void whenDeleteNotExistLineThenReturn400() {
            //When 존재하지 않는 노선을 삭제하면
            var 삭제_결과 = 노선을_삭제한다("/lines/0");

            //Then 400 를 발생시킨다
            assertBadRequest(삭제_결과.statusCode());
        }
    }

}
