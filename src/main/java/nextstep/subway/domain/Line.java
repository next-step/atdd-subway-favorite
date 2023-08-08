package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Section> getAllSections() {
        return sections.getSections();
    }
    public void addSection(Section section) {
        sections.addSection(section);
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }
    public List<Station> getStations() {
        List<Station> stations = sections.getSections().stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        stations.add(0, sections.get(0).getUpStation());
        return stations;
    }

}
