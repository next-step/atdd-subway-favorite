package nextstep.subway.path;

import nextstep.subway.line.Lines;
import nextstep.subway.station.Station;
import org.jgrapht.GraphPath;

public interface PathFinder {
    Path shortcut(Lines lines,
                  Station source,
                  Station target);

    GraphPath validCorrect(Lines lines,
                           Station source,
                           Station target);
}
