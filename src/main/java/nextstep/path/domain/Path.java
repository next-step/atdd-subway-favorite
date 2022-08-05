package nextstep.path.domain;

import nextstep.line.domain.Sections;
import nextstep.station.domain.Station;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
