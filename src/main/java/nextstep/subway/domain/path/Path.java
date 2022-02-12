package nextstep.subway.domain.path;

import nextstep.subway.domain.line.section.Sections;
import nextstep.subway.domain.station.Station;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
