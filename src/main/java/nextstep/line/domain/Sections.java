package nextstep.line.domain;

import static nextstep.global.exception.ExceptionCode.INVALID_DUPLICATE_SECTION;
import static nextstep.global.exception.ExceptionCode.INVALID_NO_EXIST_SECTION;
import static nextstep.global.exception.ExceptionCode.INVALID_SECTION_MIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.global.exception.CustomException;
import nextstep.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line", orphanRemoval = true)
    private List<Section> lineSections = new ArrayList<>();

    public Sections() {}

    public Sections(List<Section> sections) {
        this.lineSections = sections;
    }

    public void add(Section newSection) {
        validateSection(newSection);
        if (this.lineSections.isEmpty()) {
            this.lineSections.add(newSection);
            return;
        }

        Optional<Section> optionalAfterSection = this.lineSections.stream()
                .filter(section -> section.isUpStation(newSection.getUpStation()))
                .findFirst();

        if (isUpStation(newSection.getUpStation()) &&
                optionalAfterSection.isPresent()) {
            appendFirst(optionalAfterSection.get(), newSection);
            return;
        }

        if (!isUpStation(newSection.getUpStation()) &&
        optionalAfterSection.isPresent()) {
            appendCenter(optionalAfterSection.get(), newSection);
            return;
        }

        this.lineSections.add(newSection);
    }

    private void appendCenter(Section afterSection, Section newSection) {
        afterSection.updateUpStation(newSection.getDownStation());
        afterSection.decreaseDistance(newSection);
        this.lineSections.add(this.lineSections.indexOf(afterSection), newSection);
    }

    private void appendFirst(Section afterSection, Section newSection) {
        afterSection.updateDownStation(newSection.getUpStation());
        lineSections.add(0, newSection);
    }

    private void validateSection(Section newSection) {
        boolean isDuplicatedSection = this.lineSections.stream()
                .anyMatch(section -> section.isUpStation((newSection.getUpStation())) && section.isDownStation(
                        newSection.getDownStation()));

        if (isDuplicatedSection) {
            throw new CustomException(INVALID_DUPLICATE_SECTION);
        }
    }

    private boolean isDownStation(Station station) {
        if (this.lineSections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
        }
        return this.lineSections.get(this.lineSections.size() - 1).isDownStation(station);
    }


    public void deleteSection(Station station) {
        if (this.lineSections.size() < 2) {
            throw new CustomException(INVALID_SECTION_MIN);
        }

        if (isCenter(station)) {
            mergeSection(findBeforeSection(station), findAfterSection(station));
            return;
        }
        Optional<Section> optionalFirstSection = this.lineSections.stream()
                .filter(section -> section.isUpStation(station))
                .findFirst();

        optionalFirstSection.ifPresent(this::removeSection);

        Optional<Section> optionalLastSection = this.lineSections.stream()
                .filter(section -> section.isDownStation(station))
                .findFirst();

        optionalLastSection.ifPresent(this::removeSection);
    }

    private void removeSection(Section section) {
        section.remove();
        this.lineSections.remove(section);
    }

    private void mergeSection(Section beforeSection, Section afterSection) {
        beforeSection.updateDownStation(afterSection.getDownStation());
        beforeSection.increaseDistance(afterSection);
        afterSection.remove();
        this.lineSections.remove(afterSection);
    }

    private Section findBeforeSection(Station station) {
        return this.lineSections.stream().filter(section -> section.isDownStation(station)).findFirst()
                .orElseThrow(() -> new CustomException(
                        INVALID_NO_EXIST_SECTION));
    }

    private Section findAfterSection(Station station) {
        return this.lineSections.stream().filter(section -> section.isUpStation(station)).findFirst()
                .orElseThrow(() -> new CustomException(
                        INVALID_NO_EXIST_SECTION));
    }

    private boolean isCenter(Station station) {
        return !isUpStation(station) && !isDownStation(station);
    }

    private boolean isUpStation(Station station) {
        if (this.lineSections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
        }
        return this.lineSections.get(0).isUpStation(station);
    }

    public Long getUpStationId() {
        if (this.lineSections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
        }
        return this.lineSections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        if (this.lineSections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
        }
        return this.lineSections.get(this.lineSections.size() - 1).getDownStationId();
    }

    public int calculateDistance() {
        return this.lineSections.stream().mapToInt(Section::getDistance).sum();
    }

    public boolean isEmpty() {
        return this.lineSections.isEmpty();
    }

    public List<Section> getLineSections() {
        return lineSections;
    }

    public int size() {
        return lineSections.size();
    }
}
