package nextstep.line.domain;

import nextstep.section.domain.Section;
import nextstep.section.domain.Sections;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Stream;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections = new Sections();


    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        sections.add(section);
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


    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Collection<Long> getStationIds() {
        return sections.getSortedStationIds();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeStation(Long stationId) {
        sections.removeStation(stationId);
    }

    public Stream<Section> sectionStream() {
        return sections.stream();
    }

}
