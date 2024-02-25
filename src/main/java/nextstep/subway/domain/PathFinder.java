package nextstep.subway.domain;

import java.util.Optional;
import nextstep.subway.domain.vo.Path;

public interface PathFinder {

  Optional<Path> find(Station source, Station target);

  boolean isPathExists(Station source, Station target);
}
