package nextstep.subway.line.domain;

import nextstep.subway.common.ErrorMessage;
import nextstep.subway.exception.AlreadyHasUpAndDownStationException;
import nextstep.subway.exception.CannotDeleteSectionException;
import nextstep.subway.exception.NoStationException;
import nextstep.subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section newSection) {
        if (sections.isEmpty()) {
            newSection.setFirstSectionOrder();
            sections.add(newSection);
            return;
        }

        Optional<Section> upSection = findExistUpStationSection(newSection);
        Optional<Section> downSection = findExistDownStationSection(newSection);

        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new NoStationException(ErrorMessage.CANNOT_ADD_STATION);
        }

        if (upSection.isPresent() && downSection.isPresent()) {
            throw new AlreadyHasUpAndDownStationException(ErrorMessage.CANNOT_ADD_STATION);
        }

        if (upSection.isPresent()) {
            Section section = upSection.get();
            if (isAddToUpStation(newSection, section)) {
                newSection.setOrderFrontSection(section);

                section.decreaseDistance(newSection.getDistance());
                section.changeUpStation(newSection.getDownStation());
            } else {
                Section lastSection = getLastSection();
                newSection.setOrderBehindSection(lastSection);
            }
            addNewSection(newSection);
        }

        if (downSection.isPresent()) {
            Section section = downSection.get();
            if (isAddToDownStation(newSection, section)) {
                newSection.setOrderBehindSection(section);

                section.decreaseDistance(newSection.getDistance());
                section.changeDownStation(newSection.getUpStation());
            } else {
                Section firstSection = getFirstSection();
                newSection.setOrderFrontSection(firstSection);
            }
            addNewSection(newSection);
        }
    }

    private Optional<Section> findExistUpStationSection(Section section) {
        return sections.stream()
                .filter(existSection -> existSection.containStation(section.getUpStation()))
                .reduce((first, second) -> second);
    }

    private Optional<Section> findExistDownStationSection(Section section) {
        return sections.stream()
                .filter(existSection -> existSection.containStation(section.getDownStation()))
                .reduce((first, second) -> first);
    }

    private void addNewSection(Section newSection) {
        sections.forEach(currentSection -> currentSection.addOneOrder(newSection));
        sections.add(newSection);
        sections.sort(Comparator.comparingInt(Section::getLineOrder));
    }

    private boolean isAddToUpStation(Section newSection, Section section) {
        return section.getUpStation().equals(newSection.getUpStation());
    }

    private boolean isAddToDownStation(Section newSection, Section section) {
        return section.getDownStation().equals(newSection.getDownStation());
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public void deleteSection(Station station) {
        if (sections.size() <= 1) {
            throw new CannotDeleteSectionException(ErrorMessage.CANNOT_DELETE_SECTION);
        }

        List<Section> targetSections = sections.stream().filter(section -> section.containStation(station))
                .collect(Collectors.toList());

        if (targetSections.isEmpty()) {
            throw new CannotDeleteSectionException(ErrorMessage.NO_STATION_EXIST);
        }

        if (isFirstStationOrLastStation(targetSections)) {
            // 노선에 존재하는 상행역, 하행역을 삭제하는 경우
            Section section = targetSections.get(0);
            deleteExistSection(section);
        } else {
            // 노선 중간에 존재하는 역을 삭제하는 경우
            deleteMiddleStation(targetSections);
        }
    }

    private void deleteMiddleStation(List<Section> targetSections) {
        Section upSection = targetSections.get(0);
        Section downSection = targetSections.get(1);

        Section newSection = Section.joinSections(upSection, downSection);

        deleteExistSection(upSection);
        deleteExistSection(downSection);
        addNewSection(newSection);
    }

    private boolean isFirstStationOrLastStation(List<Section> targetSections) {
        return targetSections.size() == 1;
    }

    private void deleteExistSection(Section section) {
        sections.remove(section);
        sections.forEach(existSection -> existSection.minusOneOrder(section));
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private Section getFirstSection() {
        return sections.get(0);
    }
}
