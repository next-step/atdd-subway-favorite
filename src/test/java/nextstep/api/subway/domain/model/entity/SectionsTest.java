package nextstep.api.subway.domain.model.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.common.exception.subway.SectionInsertionNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/02/03
 */
class SectionsTest {

	@Test
	@DisplayName("구간 추가 - 강남역에서 역삼역까지의 구간을 성공적으로 추가한다")
	void addSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();

		// when
		sections.addSection(section);

		// then
		assertThat(sections.getSections()).contains(section);
	}

	@Test
	@DisplayName("마지막 구간 제거 - 강남역에서 역삼역까지의 구간을 성공적으로 제거한다")
	void removeLastSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		sections.removeLastSection();

		// then
		assertThat(sections.getSections()).doesNotContain(section);
	}

	@Test
	@DisplayName("구간 추가 - 강남역에서 선릉역, 서초역에서 잠실역으로의 중간 구간을 성공적으로 추가한다")
	void addMiddleSection_Success() {
		// given
		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "선릉역");
		Station station3 = new Station(3L, "서초역");
		Station station4 = new Station(4L, "잠실역");
		Section firstSection = new Section(1L, station1, station2, 10L);
		Section secondSection = new Section(2L, station3, station4, 5L);
		Sections sections = new Sections();
		sections.addSection(firstSection);

		// when
		sections.addSection(secondSection);

		// then
		assertThat(sections.getSections())
			.containsExactlyInAnyOrder(firstSection, secondSection);
	}

	@Test
	@DisplayName("구간 내 역 검색 - 역삼역을 포함하는 구간을 성공적으로 찾는다")
	void findStationInSection_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		boolean result = sections.isContainsAnyStation(2L);

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("구간으로부터 역 목록 파싱 - 강남역과 역삼역이 포함된 목록을 성공적으로 파싱한다")
	void parseStationsFromSections_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station downStation = new Station(2L, "역삼역");
		Section section = new Section(1L, upStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(section);

		// when
		List<Station> result = sections.parseStations();

		// then
		assertThat(result).containsExactlyInAnyOrder(upStation, downStation);
	}

	@Test
	@DisplayName("구간 목록에서 특정 역을 포함하는 모든 구간 찾기 - 성공 케이스")
	void findSectionsIncludingStation_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(2L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections();
		sections.addSection(firstSection);
		sections.addSection(secondSection);

		// when
		List<Section> result = sections.getSections().stream()
			.filter(section -> section.isAnyStation(middleStation.getId()))
			.collect(Collectors.toList());

		// then
		assertThat(result).containsExactlyInAnyOrder(firstSection, secondSection);
	}

	@Test
	@DisplayName("구간 목록에서 특정 역을 포함하는 모든 구간 찾기 - 선릉역을 포함하는 모든 구간을 성공적으로 찾는다")
	void excludeStationFromList_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(2L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections();
		sections.addSection(firstSection);
		sections.addSection(secondSection);

		// when
		List<Station> result = sections.parseStations().stream()
			.filter(station -> !station.getId().equals(middleStation.getId()))
			.collect(Collectors.toList());

		// then
		assertThat(result).containsExactlyInAnyOrder(upStation, downStation);
	}

	@Test
	@DisplayName("역 목록에서 특정 역을 제외한 목록 반환 - 선릉역을 제외한 역 목록을 성공적으로 반환한다")
	void validateSectionDuplication_success() {
		// Given
		Station upStation = new Station(1L, "상행역");
		Station downStation = new Station(2L, "하행역");
		Long distance = 10L;
		Section section = new Section(1L, upStation, downStation, distance);
		Sections sections = new Sections(new TreeSet<>(Set.of(section)));

		// When & then
		assertThrows(SectionInsertionNotValidException.class, () -> sections.insertSection(section));
	}

	@Test
	@DisplayName("구간 재정렬 - 구간 목록의 역 순서를 올바르게 재정렬할 수 있다")
	void reorderStationsInSection_success() {
		// Given
		Station upStation = new Station(1L, "상행역1");
		Station middleStation = new Station(2L, "중간역");
		Station downStation = new Station(3L, "하행역1");
		Long distance1 = 5L;
		Long distance2 = 7L;
		Section section1 = Section.of(upStation, middleStation, distance1);
		section1.setId(1L);
		Section section2 = Section.of(middleStation, downStation, distance2);
		section2.setId(2L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2)));

		// When
		List<Station> reorderedStations = sections.parseStations();

		// Then
		assertEquals(List.of(upStation, middleStation, downStation), reorderedStations);
	}

	@Test
	@DisplayName("구간 제거 - 중간 구간을 성공적으로 제거하여 연결된 역들이 올바르게 연결됨")
	void removeMiddleSection_Success() {
		// given

		Station station1 = new Station(1L, "강남역");
		Station station2 = new Station(2L, "선릉역");
		Station station3 = new Station(3L, "잠실역");
		Section section1 = Section.builder().id(1L).upStation(station1).downStation(station2).distance(10L).build();
		Section section2 = Section.builder().id(2L).upStation(station2).downStation(station3).distance(15L).build();
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2)));

		// when
		sections.removeStation(station2.getId());

		// then
		assertThat(sections.getSections()).hasSize(1);
		assertThat(sections.getSections().first().getUpStation()).isEqualTo(station1);
		assertThat(sections.getSections().first().getDownStation()).isEqualTo(station3);
	}

	@Test
	@DisplayName("구간 추가 - 기존 상행 종점 앞에 새로운 구간을 성공적으로 추가하여 역 목록을 업데이트함")
	void addSectionAtTheBeginning_Success() {
		// given
		Station newUpStation = new Station(0L, "신규 상행역");
		Station existingUpStation = new Station(1L, "기존 상행역");
		Station downStation = new Station(2L, "하행역");
		Section existingSection = new Section(1L, existingUpStation, downStation, 10L);
		Sections sections = new Sections();
		sections.addSection(existingSection);
		Section newSection = new Section(null, newUpStation, existingUpStation, 5L);

		// when
		sections.insertSection(newSection);

		// then
		assertThat(sections.getSections()).containsExactly(newSection, existingSection);
	}

	@Test
	@DisplayName("구간 추가 - 기존 하행 종점 뒤에 새로운 구간을 성공적으로 추가하여 역 목록을 업데이트함")
	void addSectionAtTheEnd_Success() {
		// given
		Station upStation = new Station(1L, "상행역");
		Station existingDownStation = new Station(2L, "기존 하행역");
		Station newDownStation = new Station(3L, "신규 하행역");
		Section existingSection = new Section(1L, upStation, existingDownStation, 10L);
		Sections sections = new Sections();
		sections.addSection(existingSection);
		Section newSection = new Section(null, existingDownStation, newDownStation, 5L);

		// when
		sections.insertSection(newSection);

		// then
		assertThat(sections.getSections()).containsExactly(existingSection, newSection);
	}

	@Test
	@DisplayName("구간 제거 - 상행 종점 기준으로 구간을 성공적으로 제거하여 역 목록을 업데이트함")
	void removeSectionByUpStation_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(2L, "선릉역");
		Station downStation = new Station(3L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections(new TreeSet<>(Set.of(firstSection, secondSection)));

		// when
		sections.removeStation(upStation.getId());

		// then
		assertThat(sections.getSections()).containsExactly(secondSection);
	}

	@Test
	@DisplayName("구간 제거 - 하행 종점 기준으로 구간을 성공적으로 제거하여 역 목록을 업데이트함")
	void removeSectionByDownStation_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(2L, "선릉역");
		Station downStation = new Station(3L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections(new TreeSet<>(Set.of(firstSection, secondSection)));

		// when
		sections.removeStation(downStation.getId());

		// then
		assertThat(sections.getSections()).containsExactly(firstSection);
	}

	@Test
	@DisplayName("구간 업데이트 - 특정 역 기준으로 구간을 성공적으로 업데이트하여 역 목록을 정확히 조정함")
	void updateSectionByStation_Success() {
		// given
		Station upStation = new Station(1L, "강남역");
		Station middleStation = new Station(3L, "선릉역");
		Station downStation = new Station(4L, "역삼역");
		Section firstSection = new Section(1L, upStation, middleStation, 5L);
		Section secondSection = new Section(2L, middleStation, downStation, 7L);
		Sections sections = new Sections(new TreeSet<>(Set.of(firstSection, secondSection)));

		Station newMiddleStation = new Station(2L, "새 중간역");
		Section newSection = new Section(3L, upStation, newMiddleStation, 3L);

		// when
		sections.insertSection(newSection);

		// then
		assertTrue(sections.isUpEndStation(1L));
		assertEquals(new ArrayList<>(sections.getSections()).get(1).getUpStation().getName(), "새 중간역");
		assertTrue(sections.isDownEndStation(4L));
	}

	@Test
	@DisplayName("주어지는 두 역이 유효한 구간 내에 동시에 존재하는지 검증한다 1")
	void isContainsBothAsValidTest_1() {
		// given
		Station upStation = new Station(1L, "1");
		Station middleStation = new Station(2L, "2");
		Station downStation = new Station(3L, "3");
		Section section1 = new Section(1L, upStation, middleStation, 10L);
		Section section2 = new Section(2L, middleStation, downStation, 15L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2)));

		// when
		boolean result = sections.isContainsBothAsValid(upStation.getId(), downStation.getId());

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("주어지는 두 역이 유효한 구간 내에 동시에 존재하는지 검증한다 2")
	void isContainsBothAsValidTest_2() {
		// given
		Station upStation = new Station(1L, "1");
		Station secondStation = new Station(2L, "2");
		Station thirdStation = new Station(3L, "3");
		Station fourthStation = new Station(4L, "4");
		Station downStation = new Station(5L, "5");
		Section section1 = new Section(1L, upStation, secondStation, 10L);
		Section section2 = new Section(2L, secondStation, thirdStation, 15L);
		Section section3 = new Section(3L, thirdStation, fourthStation, 20L);
		Section section4 = new Section(4L, fourthStation, downStation, 25L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2, section3, section4)));

		// when
		boolean result = sections.isContainsBothAsValid(upStation.getId(), downStation.getId());

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("두 역이 유효한 구간 내에 동시에 존재하지 않는 경우를 검증")
	void isNotContainsBothAsValidTest() {
		// given
		Station upStation = new Station(1L, "1");
		Station middleStation = new Station(2L, "2");
		Station downStation = new Station(3L, "3");
		Station anotherStation = new Station(4L, "4");
		Section section1 = new Section(1L, upStation, middleStation, 10L);
		Section section2 = new Section(2L, middleStation, downStation, 15L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2)));

		// when
		boolean result = sections.isContainsBothAsValid(upStation.getId(), anotherStation.getId());

		// then
		assertFalse(result);
	}

	@Test
	@DisplayName("주어진 두 역이 동일한 경우 유효한 구간 내에 존재하지 않은 것으로 판단해야 한다")
	void isContainsBothAsValid_SameStationTest() {
		// given
		Station station1 = new Station(1L, "1");
		Station station2 = new Station(2L, "2");
		Section section1 = new Section(1L, station1, station2, 10L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1)));

		// when
		boolean result = sections.isContainsBothAsValid(station1.getId(), station1.getId());

		// then
		assertFalse(result);
	}

	@Test
	@DisplayName("중간 역이 제거된 후 두 역이 유효한 구간 내에 동시에 존재하는지 검증")
	void isContainsBothAsValid_AfterRemovingMiddleStationTest() {
		// given
		Station station1 = new Station(1L, "1");
		Station station2 = new Station(2L, "2");
		Station station3 = new Station(3L, "3");
		Station station4 = new Station(4L, "4");
		Section section1 = new Section(1L, station1, station2, 10L);
		Section section2 = new Section(2L, station2, station3, 15L);
		Section section3 = new Section(3L, station3, station4, 20L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section1, section2, section3)));
		sections.removeStation(station2.getId());

		// when
		boolean result = sections.isContainsBothAsValid(station1.getId(), station4.getId());

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("복잡한 구조에서 두 역이 유효한 구간 내에 동시에 존재하는지 검증 - 순서 뒤바뀐 케이스")
	void isContainsBothAsValid_ComplexCase() {
		// given
		Station stationA = new Station(1L, "A");
		Station stationB = new Station(2L, "B");
		Station stationC = new Station(3L, "C");
		Station stationD = new Station(4L, "D");
		Station stationE = new Station(5L, "E");
		Section section1 = new Section(1L, stationA, stationB, 10L);
		Section section2 = new Section(2L, stationB, stationC, 15L);
		Section section3 = new Section(3L, stationD, stationE, 20L);
		Section section4 = new Section(4L, stationC, stationD, 25L);
		Sections sections = new Sections(new TreeSet<>(Set.of(section2, section1, section4, section3)));

		// when
		boolean result = sections.isContainsBothAsValid(stationA.getId(), stationE.getId());

		// then
		assertTrue(result);
	}

}
