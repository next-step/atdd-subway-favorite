package nextstep.api.subway.domain.operators;

import java.util.List;

import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.model.vo.Path;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
public interface PathFinder {

	Path findShortestPath(Station sourceStation, Station targetStation, List<Section> sections);
}
