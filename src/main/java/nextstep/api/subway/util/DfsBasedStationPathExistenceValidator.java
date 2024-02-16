package nextstep.api.subway.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import nextstep.api.subway.domain.model.entity.Section;

/**
 * StationPathExistenceValidator는 주어진 두 스테이션 사이에 유효한 경로가 존재하는지 검증합니다.
 * 이 클래스는 Sections 일급 컬렉션 내부의 복잡한 알고리즘을 캡슐화하여 Sections의 가독성과 유지보수성을 향상시키는 데 목적이 있습니다.
 *
 * @author : Rene Choi
 * @since : 2024/02/16
 */
public class DfsBasedStationPathExistenceValidator {

	public static boolean isValidPathBetweenStations(SortedSet<Section> sections, Long sourceStationId, Long targetStationId) {
		Set<Long> visited = new HashSet<>();
		Deque<Long> stack = new ArrayDeque<>();
		stack.push(sourceStationId);
		visited.add(sourceStationId);

		while (!stack.isEmpty()) {
			Long currentStationId = stack.pop();
			if (currentStationId.equals(targetStationId)) {
				return true;
			}

			for (Section section : sections) {
				if (section.fetchUpStationId().equals(currentStationId) && visited.add(section.fetchDownStationId())) {
					stack.push(section.fetchDownStationId());
				} else if (section.fetchDownStationId().equals(currentStationId) && visited.add(section.fetchUpStationId())) {
					stack.push(section.fetchUpStationId());
				}
			}
		}

		return false;
	}
}
