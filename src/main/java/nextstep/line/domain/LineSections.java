package nextstep.line.domain;

import nextstep.line.domain.exception.SectionException;
import nextstep.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class LineSections {
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected LineSections() {
    }

    public List<Section> getSectionsInOrder() {
        return sections.stream().sorted().collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return getSectionsInOrder()
                .stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void addSection(Section sectionToAdd) {
        if (sections.isEmpty()) {
            sections.add(sectionToAdd);
            return;
        }

        if (sectionToAdd.getUpStation().equals(sectionToAdd.getDownStation())) {
            throw new SectionException("상행역과 하행역은 동일할 수 없습니다.");
        }

        if (isToBeFirstSection(sectionToAdd)) {
            addAsFirstSection(sectionToAdd);
            return;
        }

        Optional<Section> sectionWithSameUpStation = getSectionWithSameUpStation(sectionToAdd);
        Optional<Section> sectionWithSameDownStation = getSectionWithSameDownStation(sectionToAdd);

        if (sectionWithSameUpStation.isPresent()) {
            addAsMiddleSection(sectionToAdd, sectionWithSameUpStation.get());
            return;
        }
        if (sectionWithSameDownStation.isPresent()) {
            addAsMiddleSection(sectionToAdd, sectionWithSameDownStation.get());
            return;
        }

        addAsLastSection(sectionToAdd);
    }

    public void deleteSection(Long stationId) {
        validateDeleteStation(stationId);

        if (getFirstStation().getId().equals(stationId)) {
            sections.remove(getFirstSection());
            return;
        }

        if (getLastStation().getId().equals(stationId)) {
            sections.remove(getLastSection());
            return;
        }
        deleteMiddleSectionByStationId(stationId);
    }

    private boolean isToBeFirstSection(Section section) {
        return section.getDownStation().equals(getFirstStation());
    }

    private void addAsFirstSection(Section section) {
        if (getStations().contains(section.getUpStation())) {
            throw new SectionException("상행역은 기존 노선에 존재하는 역일 수 없습니다.");
        }
        sections.add(section);
    }

    private Optional<Section> getSectionWithSameUpStation(Section section) {
        return sections.stream()
                .filter(s -> s.getUpStation().equals(section.getUpStation()))
                .findFirst();
    }

    private Optional<Section> getSectionWithSameDownStation(Section section) {
        return sections.stream()
                .filter(s -> s.getDownStation().equals(section.getDownStation()))
                .findFirst();
    }

    private void addAsMiddleSection(Section sectionToAdd, Section existingSection) {
        boolean isSameUpStation = existingSection.getUpStation().equals(sectionToAdd.getUpStation());
        boolean isSameDownStation = existingSection.getDownStation().equals(sectionToAdd.getDownStation());

        if (isSameUpStation && isSameDownStation) {
            throw new SectionException("이미 존재하는 구간입니다.");
        }
        if (existingSection.getDistance() <= sectionToAdd.getDistance()) {
            throw new SectionException("추가하려는 구간의 거리가 기존 구간보다 큽니다.");
        }

        if (isSameUpStation && !isSameDownStation) {
            Section splitExistingSection = Section.create(
                    sectionToAdd.getUpStation(),
                    existingSection.getDownStation(),
                    existingSection.getDistance() - sectionToAdd.getDistance());

            sections.remove(existingSection);
            sections.add(sectionToAdd);
            sections.add(splitExistingSection);
            return;
        }

        Section splitExistingSection = Section.create(
                existingSection.getUpStation(),
                sectionToAdd.getUpStation(),
                existingSection.getDistance() - sectionToAdd.getDistance());

        sections.remove(existingSection);
        sections.add(splitExistingSection);
        sections.add(sectionToAdd);
    }

    private void addAsLastSection(Section section) {
        if (getStations().contains(section.getDownStation())) {
            throw new SectionException("노선의 마지막 구간을 추가하려는 경우 하행역은 기존 노선에 없는 역이여야 합니다.");
        }
        if (!section.getUpStation().equals(getLastStation())) {
            throw new SectionException("노선의 하행역만 상행역으로 지정 가능합니다.");
        }
        sections.add(section);
    }

    private void validateDeleteStation(Long stationId) {
        if (sections.size() <= 1) {
            throw new SectionException("삭제할 수 있는 구간이 없습니다.");
        }

        if (getStations().stream().noneMatch(station -> station.getId().equals(stationId))) {
            throw new SectionException("노선에 존재하지 않는 역입니다.");
        }
    }

    private void deleteMiddleSectionByStationId(Long stationId) {
        Section sectionWithStationIdAsDownStation = sections.stream()
                .filter(section -> section.getDownStation().getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new SectionException("노선에 존재하지 않는 역입니다."));

        Section sectionWithStationIdAsUpStation = sections.stream()
                .filter(section -> section.getUpStation().equals(sectionWithStationIdAsDownStation.getDownStation()))
                .findFirst()
                .orElseThrow(() -> new SectionException(stationId + " 역의 다음 구간이 존재하지 않습니다."));

        int deletedSectionsDistance = sectionWithStationIdAsUpStation.getDistance()
                + sectionWithStationIdAsDownStation.getDistance();

        Section sectionForReconnection = Section.create(
                sectionWithStationIdAsDownStation.getUpStation(),
                sectionWithStationIdAsUpStation.getDownStation(),
                deletedSectionsDistance);

        sections.remove(sectionWithStationIdAsDownStation);
        sections.remove(sectionWithStationIdAsUpStation);
        sections.add(sectionForReconnection);
    }

    private Station getFirstStation() {
        return getStations().get(0);
    }

    private Section getFirstSection() {
        return getSectionsInOrder().get(0);
    }

    private Station getLastStation() {
        return getStations().get(getStations().size() - 1);
    }

    private Section getLastSection() {
        return getSectionsInOrder().get(getSectionsInOrder().size() - 1);
    }
}
