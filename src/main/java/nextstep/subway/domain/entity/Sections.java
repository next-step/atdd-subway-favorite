package nextstep.subway.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import nextstep.exception.ApplicationException;
import org.springframework.util.ObjectUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.exception.ExceptionMessage.*;

@Embeddable
public class Sections {
    @JsonValue
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void addSection(Section newSection) {
        // 추가하려는 구간의 상행역 하행역이 같으면
        if (newSection.getUpStation().equals(newSection.getDownStation())) {
            throw new ApplicationException(NEW_SECTION_VALIDATION_EXCEPTION.getMessage());
        }

        if (this.sections.isEmpty()) {
            this.sections.add(newSection);
            return;
        }
        // 이미 등록된 구간인지
        if (isRegisteredSection(newSection)) {
            throw new ApplicationException(ALREADY_REGISTERED_SECTION_EXCEPTION.getMessage());
        }

        // 새로운 구간과 동일한 상행역이 있는지
        Station upStation = newSection.getUpStation();
        if (this.sections.stream().anyMatch(section -> section.getUpStation().equals(upStation))) {
            insertSection(newSection, getSameUpStationSection(upStation));
            return;
        }
        // 새로운 구간과 동일한 하행역이 있는지
        Station downStation = newSection.getDownStation();
        if (sections.stream().anyMatch(section -> section.getDownStation().equals(downStation))) {
            insertSection(newSection, getSameDownStationSection(downStation));
            return;
        }
        sections.add(newSection);
    }

    private void insertSection(Section newSection, Section basedSection) {
        checkValidation(newSection, basedSection);
        int newDistance = basedSection.getDistance() - newSection.getDistance();

        this.sections.remove(basedSection);
        this.sections.add(newSection);

        // 상행역이 같다면
        if (basedSection.isSameAsUpStation(newSection.getUpStation())) {
            this.sections.add(new Section(newSection.getDownStation(), basedSection.getDownStation(), newDistance));
            return;
        }
        // 하행역이 같다면
        if (basedSection.isSameAsDwonStation(newSection.getDownStation())) {
            this.sections.add(new Section(basedSection.getUpStation(), newSection.getUpStation(), newDistance));
        }
    }

    private void checkValidation(Section newSection, Section basedSection) {
        // 기존 구간보다 거리가 길거나 같은 노선인지
        if (newSection.getDistance() >= basedSection.getDistance()) {
            throw new ApplicationException(LONGER_DISTANCE_SECTION_EXCEPTION.getMessage());
        }
    }

    public void deleteSection(Section section) {
        this.sections.remove(section);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public Section getLastSection() {
        return this.sections.stream()
                .filter(this::isLastSection)
                .findFirst()
                .orElseThrow(() -> new ApplicationException(NO_EXISTS_LAST_SECTION_EXCEIPTION.getMessage()));
    }

    public int getSize() {
        return this.sections.size();
    }

    private Section getSameUpStationSection(Station station) {
        Optional<Section> findSection = sections.stream()
                .filter(section -> section.isSameAsUpStation(station))
                .findFirst();

        return findSection.orElse(null);
    }

    private Section getSameDownStationSection(Station station) {
        Optional<Section> findSection = sections.stream()
                .filter(section -> section.isSameAsDwonStation(station))
                .findFirst();

        return findSection.orElse(null);
    }

    private boolean isFirstSection(Section basedSection) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation().equals(basedSection.getUpStation()));
    }

    private boolean isLastSection(Section basedSection) {
        return sections.stream()
                .noneMatch(section -> section.getUpStation().equals(basedSection.getDownStation()));
    }

    public int getDistance() {
        int sumOfDistance = 0;
        for (Section section : this.sections) {
            sumOfDistance += section.getDistance();
        }
        return sumOfDistance;
    }

    public boolean isRegisteredSection(Section section) {
        boolean isExistUpStation = this.getStations().stream()
                .anyMatch(station -> station.equals(section.getUpStation()));

        boolean isExistDownStation = this.getStations().stream()
                .anyMatch(station -> station.equals(section.getDownStation()));

        return isExistUpStation && isExistDownStation;
    }


    public void deleteStation(Station station) {
        // 삭제할 역을 하행역으로 가진 구간 찾기
        Section section1 = getSameDownStationSection(station);
        // 삭제할 역을 상행역으로 가진 구간 찾기
        Section section2 = getSameUpStationSection(station);

        // 노선의 상행종점역인 경우 (삭제할 역을 상행역으로 가진 구간만 존재)
        if (ObjectUtils.isEmpty(section1) && !ObjectUtils.isEmpty(section2)) {
            // section2 노선 제거
            sections.remove(section2);
        }
        // 노선의 하행종점역인 경우 (삭제할 역을 하행역으로 가진 구간만 존재)
        if (!ObjectUtils.isEmpty(section1) && ObjectUtils.isEmpty(section2)) {
            // section1 노선 제거
            sections.remove(section1);
        }
        // 노선의 중간 역을 제거할 경우
        if (!ObjectUtils.isEmpty(section1) && !ObjectUtils.isEmpty(section2)) {
            // 삭제할 역을 포함한 구간의 상행역 - 하행역 구간 추가
            Section newSection = new Section(section1.getLine(), section1.getUpStation(), section2.getDownStation(), section1.getDistance() + section2.getDistance());
            sections.add(newSection);
            // 삭제할 역을 포함한 구간 제거
            sections.remove(section1);
            sections.remove(section2);
        }
    }
}
