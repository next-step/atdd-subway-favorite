package nextstep.line.domain;

import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private final LineSections sections = new LineSections();

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    protected Line() {
    }

    public static Line create(String name, String color) {
        return new Line(name, color);
    }

    public static Line create(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line(name, color);
        return getSectionAddedLine(line, upStation, downStation, distance);
    }

    private static Line getSectionAddedLine(Line line, Station upStation, Station downStation, int distance) {
        Section section = Section.create(upStation, downStation, distance);
        line.sections.addSection(section);
        return line;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
    }

    public void deleteSection(Long stationId) {
        this.sections.deleteSection(stationId);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public List<Section> getSections() {
        return this.sections.getSectionsInOrder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
