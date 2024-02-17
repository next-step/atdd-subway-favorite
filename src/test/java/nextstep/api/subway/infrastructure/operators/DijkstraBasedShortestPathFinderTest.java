package nextstep.api.subway.infrastructure.operators;

import static nextstep.fixture.SectionFixtureCreator.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jdk.jfr.Description;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.model.vo.Path;
import nextstep.common.exception.subway.PathNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/02/11
 */
@ExtendWith(MockitoExtension.class)
class DijkstraBasedShortestPathFinderTest {

	@InjectMocks
	private DijkstraBasedShortestPathFinder dijkstraBasedShortestPathFinder;

	@Test
	@DisplayName("지하철 최단 경로 조회 1: 주어진 구간에 대해 최단 경로와 거리를 정확히 계산한다")
	void findShortestPathTest_1() {
		// Given
		Station station1 = new Station(1L, "Station1");
		Station station2 = new Station(2L, "Station2");
		Station station3 = new Station(3L, "Station3");
		Section section1 = createSectionWithIdRandom(station1, station2, 10L);
		Section section2 = createSectionWithIdRandom(station2, station3, 20L);

		List<Section> sections = Arrays.asList(section1, section2);

		// When
		Path result = dijkstraBasedShortestPathFinder.findShortestPath(station1, station3, sections);

		// Then
		assertNotNull(result);
		assertEquals(Arrays.asList(station1, station2, station3), result.getStations());
		assertEquals(30, result.getDistance());
	}

	/**
	 * Station1 --10--> Station2 --15--> Station3 --20--> Station4
	 *   					|                               |
	 *   					5                               |
	 *   					|                               |
	 *   					v                               |
	 * 					 Station5 -----------------------> 10
	 */
	@Test
	@DisplayName("지하철 최단 경로 조회 2: 주어진 구간에 대해 최단 경로와 거리를 정확히 계산한다")
	@Description("station1에서 station4로 가는 경로 중 station2를 경유해 station5를 거쳐 station4로 가는 최단 경로를 찾는 경우.")
	void findShortestPathTest_2() {
		// Given
		Station station1 = new Station(1L, "Station1");
		Station station2 = new Station(2L, "Station2");
		Station station3 = new Station(3L, "Station3");
		Station station4 = new Station(4L, "Station4");
		Station station5 = new Station(5L, "Station5");
		Section section1 = createSectionWithIdRandom(station1, station2, 10L);
		Section section2 = createSectionWithIdRandom(station2, station3, 15L);
		Section section3 = createSectionWithIdRandom(station3, station4, 20L);
		Section section4 = createSectionWithIdRandom(station2, station5, 5L);
		Section section5 = createSectionWithIdRandom(station5, station4, 10L);

		List<Section> sections = Arrays.asList(section1, section2, section3, section4, section5);

		// When
		Path result = dijkstraBasedShortestPathFinder.findShortestPath(station1, station4, sections);

		// Then
		assertNotNull(result);
		assertEquals(Arrays.asList(station1, station2, station5, station4), result.getStations());
		assertEquals(25, result.getDistance());
	}

	/**
	 * Station1 --5--> Station2 --10--> Station3 --3--> Station4 --4--> Station5
	 *                     |                               ^
	 *                     2                               |
	 *                     |                               |
	 *                     v                               |
	 *                Station6 --------------------------> 8
	 *                     ^
	 *                     |
	 *                     1
	 *                     |
	 *                 Station5
	 */
	@Test
	@DisplayName("지하철 최단 경로 조회 3: 주어진 구간에 대해 최단 경로와 거리를 정확히 계산한다")
	@Description("station1에서 station5까지 가는 경로에서 여러 가능한 경로 중 최단 경로를 계산한다. station1 -> station2 -> station6 -> station4 -> station5의 경로가 최단 경로.")
	void findShortestPathTest_3() {
		// Given
		Station station1 = new Station(1L, "Station1");
		Station station2 = new Station(2L, "Station2");
		Station station3 = new Station(3L, "Station3");
		Station station4 = new Station(4L, "Station4");
		Station station5 = new Station(5L, "Station5");
		Station station6 = new Station(6L, "Station6");
		Section section1 = createSectionWithIdRandom(station1, station2, 5L);
		Section section2 = createSectionWithIdRandom(station2, station3, 10L);
		Section section3 = createSectionWithIdRandom(station3, station4, 3L);
		Section section4 = createSectionWithIdRandom(station4, station5, 4L);
		Section section5 = createSectionWithIdRandom(station2, station6, 2L);
		Section section6 = createSectionWithIdRandom(station6, station4, 8L);
		Section section7 = createSectionWithIdRandom(station5, station6, 1L);

		List<Section> sections = Arrays.asList(section1, section2, section3, section4, section5, section6, section7);

		// When
		Path result = dijkstraBasedShortestPathFinder.findShortestPath(station1, station5, sections);

		// Then
		assertNotNull(result);
		assertEquals(Arrays.asList(station1, station2, station6, station4, station5), result.getStations());
		assertEquals(19, result.getDistance());
	}

	@Test
	@DisplayName("단순 경로 존재 여부 확인: 주어진 구간에 대해 두 역 사이에 경로가 존재하는지 확인한다")
	void testPathExistence_1() {
		// Given
		Station station1 = new Station(1L, "Station1");
		Station station2 = new Station(2L, "Station2");
		Section section = createSectionWithIdRandom(station1, station2, 10L);

		List<Section> sections = Arrays.asList(section);

		// When & Then
		assertDoesNotThrow(() -> {
			Path result = dijkstraBasedShortestPathFinder.findShortestPath(station1, station2, sections);
			assertNotNull(result);
			assertTrue(result.getStations().containsAll(Arrays.asList(station1, station2)));
		}, "두 역 사이에 경로가 존재해야 한다.");
	}

	@Test
	@DisplayName("단순 경로 존재 여부 확인: 주어진 구간에 대해 두 역 사이에 경로가 존재하지 않는 경우를 확인한다")
	void testPathExistence_2() {
		// Given
		Station station1 = new Station(1L, "Station1");
		Station station2 = new Station(2L, "Station2");
		Station station3 = new Station(3L, "Station3"); // 이 역은 다른 역들과 연결되지 않음
		Section section = createSectionWithIdRandom(station1, station2, 10L);

		List<Section> sections = Arrays.asList(section);

		// When & Then
		assertThrows(PathNotValidException.class, () -> {
			dijkstraBasedShortestPathFinder.findShortestPath(station1, station3, sections);
		}, "두 역 사이에 경로가 존재하지 않으면 PathNotValidException 예외가 발생해야 한다.");
	}






}
