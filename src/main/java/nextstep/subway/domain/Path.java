package nextstep.subway.domain;

import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
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
