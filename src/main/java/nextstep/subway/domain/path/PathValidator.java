package nextstep.subway.domain.path;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import nextstep.utils.exception.PathNotLinkedException;

import java.util.List;

public class PathValidator {

    public static void validateNotLinked(List<Line> lines, Station upStation, Station downStation) {
        SubwayMap subwayMap = new SubwayMap(lines);
        if (subwayMap.isLinked(upStation, downStation)) {
            return;
        }
        throw new PathNotLinkedException();
    }
}
