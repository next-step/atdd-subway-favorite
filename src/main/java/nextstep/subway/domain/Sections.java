package nextstep.subway.domain;

import nextstep.subway.exception.InvalidSectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    public void deleteLastSection() {
        if (sections.isEmpty()) {
            throw new InvalidSectionException("삭제할 수 있는 지하철 구간이 없습니다.");
        }
        sections.remove(sections.size() - 1);
    }

    void addSection(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        List<Station> stations = getStations();
        Station upStation = newSection.getUpStation();
        Station downStation = newSection.getDownStation();
        boolean isUpStationConnected = stations.contains(upStation);
        boolean isDownStationConnected = stations.contains(downStation);

        validateStationConnections(isUpStationConnected, isDownStationConnected);

        if (isUpStationConnected) {
            addSectionWithConnectedUpStation(newSection);
            return;
        }
        addSectionWithConnectedDownStation(newSection);
    }

    private void addSectionWithConnectedUpStation(Section newSection) {
        Station newUpStation = newSection.getUpStation();
        sections.stream()
                .filter((section) -> section.getUpStation().equals(newUpStation))
                .findFirst()
                .ifPresentOrElse(
                        (section) -> updateSection(section, newSection, true),
                        () -> sections.add(newSection)
                );
    }

    private void addSectionWithConnectedDownStation(Section newSection) {
        Station newDownStation = newSection.getDownStation();
        sections.stream()
                .filter((section) -> section.getDownStation().equals(newDownStation))
                .findFirst()
                .ifPresentOrElse(
                        (section) -> updateSection(section, newSection, false),
                        () -> sections.add(0, newSection)
                );
    }

    private void updateSection(Section existingSection, Section newSection, boolean isUpStationConnected) {
        validateSectionDistance(existingSection, newSection);

        Section updatedSection = createUpdatedSection(existingSection, newSection, isUpStationConnected);

        sections.add(newSection);
        sections.add(updatedSection);
        sections.remove(existingSection);
    }

    private void validateStationConnections(boolean isUpStationConnected, boolean isDownStationConnected) {
        if (!isUpStationConnected && !isDownStationConnected) {
            throw new InvalidSectionException("새로운 구간의 양쪽 역 모두 기존 노선에 연결되어 있지 않습니다.");
        }
        if (isUpStationConnected && isDownStationConnected) {
            throw new InvalidSectionException("새로운 구간이 기존 구간을 완전히 포함합니다.");
        }
    }

    private void validateSectionDistance(Section existingSection, Section newSection) {
        if (existingSection.getSectionDistance().isLessThanOrEqualTo(newSection.getSectionDistance())) {
            throw new InvalidSectionException("기존 구간의 길이가 새 구간보다 길어야 합니다.");
        }
    }

    private Section createUpdatedSection(Section existingSection, Section newSection, boolean isUpStationConnected) {
        Station updatedUpStation = getUpdatedUpStation(existingSection, newSection, isUpStationConnected);
        Station updatedDownStation = getUpdatedDownStation(existingSection, newSection, isUpStationConnected);
        int updatedDistance = existingSection.getSectionDistance().minus(newSection.getSectionDistance()).getDistance();

        return Section.createSection(
                existingSection.getLine(),
                updatedUpStation,
                updatedDownStation,
                updatedDistance
        );
    }

    private Station getUpdatedUpStation(Section existingSection, Section newSection, boolean isUpStationConnected) {
        if (isUpStationConnected) {
            return newSection.getDownStation();
        }
        return existingSection.getUpStation();
    }

    private Station getUpdatedDownStation(Section existingSection, Section newSection, boolean isUpStationConnected) {
        if (isUpStationConnected) {
            return existingSection.getDownStation();
        }
        return newSection.getUpStation();
    }

    void deleteStation(Station station) {
        if (!this.getStations().contains(station)) {
            throw new InvalidSectionException("노선에 포함되지 않은 역을 제거할 수 없습니다.");
        }

        Optional<Section> previousSection = findSectionByDownStation(station);
        Optional<Section> nextSection = findSectionByUpStation(station);

        if (previousSection.isEmpty() && nextSection.isEmpty()) {
            throw new InvalidSectionException("삭제할 수 있는 지하철 구간이 없습니다.");
        }

        if (previousSection.isPresent() && nextSection.isEmpty()) {
            sections.remove(previousSection.get());
            return;
        }

        if (previousSection.isEmpty()) {
            sections.remove(nextSection.get());
            return;
        }

        mergeSections(previousSection.get(), nextSection.get());
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst();
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }

    private void mergeSections(Section previousSection, Section nextSection) {
        Section mergedSection = Section.createSection(
                previousSection.getLine(),
                previousSection.getUpStation(),
                nextSection.getDownStation(),
                previousSection.getSectionDistance().plus(nextSection.getSectionDistance()).getDistance()
        );
        sections.remove(previousSection);
        sections.remove(nextSection);
        sections.add(mergedSection);
    }
}
