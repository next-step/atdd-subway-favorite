package nextstep.api.subway.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.operators.LineResolver;
import nextstep.api.subway.domain.operators.PathFinder;
import nextstep.api.subway.domain.operators.StationResolver;
import nextstep.api.subway.domain.service.PathService;
import nextstep.api.subway.interfaces.dto.response.PathResponse;
import nextstep.common.exception.subway.PathNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimplePathService implements PathService {
	private final StationResolver stationResolver;
	private final PathFinder pathFinder;

	private final LineResolver lineResolver;

	/**
	 * source station과 target station이 주어질 때 최단거리를 찾아주는 서비스
	 * 다익스트라 알고리즘을 통해 최단 거리 탐색시에는 node와 edge에 대한 정보가 필요하며 이는 station과 section에 대응된다.
	 * 따라서 찾고자 하는 station에 대해 관련된 section을 불러오고 (다익스트라 알고리즘을 통해 최단 거리를 찾을 것이므로 여기서 순서는 상관없음)
	 * 해당 값들을 파라미터로 pathFinder에게 넘겨주어 최단거리 탐색 알고리즘을 수행하여 최단거리를 찾아 Path 객체로 반환 받는다.
	 * <p>
	 * 노선의 개수를 불러오는 부분에서 모든 노선을 불러와 어플리케이션 연산으로 관련된 Section을 탐색하는데
	 * db join연산으로 불러오는 방식과의 차이를 고려하여
	 * 성능 이슈를 고려해보아야 한다.
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	@Override
	public PathResponse findShortestPath(Long source, Long target) {
		if (source.equals(target)) {
			throw new PathNotValidException("Source and target stations cannot be the same.");
		}

		Station sourceStation = stationResolver.fetchOptional(source).orElseThrow(PathNotValidException::new);
		Station targetStation = stationResolver.fetchOptional(target).orElseThrow(PathNotValidException::new);

		List<Line> lines = lineResolver.fetchAll();

		List<Section> sections = lines.stream()
			.filter(line -> line.isContainsAnyStation(source, target))
			.map(Line::parseSections)
			.flatMap(java.util.Collection::stream)
			.collect(Collectors.toList());

		return PathResponse.from(pathFinder.findShortestPath(sourceStation, targetStation, sections));
	}

}
