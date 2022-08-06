package nextstep.line.domain;

import nextstep.line.domain.exception.CantAddSectionException;
import nextstep.line.domain.exception.CantDeleteSectionException;
import nextstep.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        checkDuplicateSection(section);

        rearrangeSectionWithUpStation(section);
        rearrangeSectionWithDownStation(section);

        sections.add(section);
    }

    public void delete(Station station) {
        if (sections.size() <= 1) {
            throw new CantDeleteSectionException("구간이 둘 이상일때만 삭제할 수 있습니다.");
        }

        Optional<Section> upSection = findSectionAsUpStation(station);
        Optional<Section> downSection = findSectionAsDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            addNewSectionForDelete(upSection.get(), downSection.get());
        }

        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        Station upStation = findFirstUpStation();
        List<Station> result = new ArrayList<>();
        result.add(upStation);

        while (true) {
            Station finalUpStation = upStation;
            Optional<Section> section = findSectionAsUpStation(finalUpStation);

            if (section.isEmpty()) {
                break;
            }

            upStation = section.get().getDownStation();
            result.add(upStation);
        }

        return result;
    }

    public int totalDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public List<Section> getSections() {
        return sections;
    }

    private void checkDuplicateSection(Section section) {
        sections.stream()
                .filter(it -> it.hasDuplicateSection(section.getUpStation(), section.getDownStation()))
                .findFirst()
                .ifPresent(it -> {
                    throw new CantAddSectionException("이미 노선에 포함된 구간입니다.");
                });
    }

    private void rearrangeSectionWithUpStation(Section section) {
        sections.stream()
                .filter(it -> it.isSameUpStation(section.getUpStation()))
                .findFirst()
                .ifPresent(it -> {
                    // 신규 구간의 하행역과 기존 구간의 하행역에 대한 구간을 추가한다.
                    sections.add(new Section(section.getLine(), section.getDownStation(), it.getDownStation(), it.getDistance() - section.getDistance()));
                    sections.remove(it);
                });
    }

    private void rearrangeSectionWithDownStation(Section section) {
        sections.stream()
                .filter(it -> it.isSameDownStation(section.getDownStation()))
                .findFirst()
                .ifPresent(it -> {
                    // 신규 구간의 상행역과 기존 구간의 상행역에 대한 구간을 추가한다.
                    sections.add(new Section(section.getLine(), it.getUpStation(), section.getUpStation(), it.getDistance() - section.getDistance()));
                    sections.remove(it);
                });
    }

    private Optional<Section> findSectionAsUpStation(Station finalUpStation) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(finalUpStation))
                .findFirst();
    }

    private Optional<Section> findSectionAsDownStation(Station station) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(station))
                .findFirst();
    }

    private void addNewSectionForDelete(Section upSection, Section downSection) {
        Section newSection = new Section(
                upSection.getLine(),
                downSection.getUpStation(),
                upSection.getDownStation(),
                upSection.getDistance() + downSection.getDistance()
        );

        sections.add(newSection);
    }

    private Station findFirstUpStation() {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        List<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return upStations.stream()
                .filter(it -> !downStations.contains(it))
                .findFirst()
                .orElseThrow();
    }
}
