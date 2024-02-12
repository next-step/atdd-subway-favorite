package nextstep.line;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.exception.InvalidInputException;
import nextstep.section.Section;
import nextstep.section.Sections;
import nextstep.station.Station;
import org.jgrapht.alg.util.Pair;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Slf4j
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Sections sections = new Sections();

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public int getDistance() {
        return sections.getTotalDistance();
    }

    public Section initSection(Station upstation, Station downstation, int distance) {
        Section section = Section.initSection(this, upstation, downstation, distance);
        sections.initSection(section);
        return section;
    }

    public void addSection(Section newSection) {
        /*
         * newSection의 upstation이 line의 하행종점역이면
         * addLastSection() 호출
         * newSection의 upstation이 line의 상행종점역이면
         * addFirstSection() 호출
         * 이 둘 중 아무것도 아니면
         * addSection() 호출
         * */
        boolean upstationExists = sections.stream().anyMatch(section ->
                section.isUpstation(newSection.getUpstation()) ||
                        section.isDownstation(newSection.getUpstation()));
        boolean downstationExists = sections.stream().anyMatch(section ->
                section.isUpstation(newSection.getDownstation()) ||
                        section.isDownstation(newSection.getDownstation()));

        if (upstationExists && downstationExists) {
            throw new InvalidInputException("새로운 구간의 상행역과 하행역 둘 다 이미 노선에 등록되어 있습니다.");
        }

        if (sections.isFirstUpstation(newSection.getDownstation())) {
            sections.addFirstSection(newSection);
        } else if(sections.isLastDownstation(newSection.getUpstation())) {
            sections.addLastSection(newSection);
        } else {
            sections.addSection(newSection);
        }

    }

    public void removeSection(Station station) {
        if (sections.size() == 1) {
            throw new InvalidInputException("노선에 상행 종점역과 하행 종점역만 있는 경우에는 제거할 수 없습니다.");
        }

        if (sections.isFirstUpstation(station)) {
            sections.removeFirstSection();
        } else if (sections.isLastDownstation(station)) {
            sections.removeLastSection();
        } else {
            sections.removeSection(station);
        }
    }

    @Builder
    public Line(Long id, String color, String name, Sections sections) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.sections = sections != null ? sections : new Sections();
    }
}
