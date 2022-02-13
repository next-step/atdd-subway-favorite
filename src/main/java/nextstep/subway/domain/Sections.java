package nextstep.subway.domain;

import nextstep.exception.NotFoundStationException;
import nextstep.exception.ValidationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.error.ErrorCode.ALREADY_EXISTS_STATIONS_ERROR;
import static nextstep.error.ErrorCode.FIRST_SECTION_CREATE_ERROR;
import static nextstep.error.ErrorCode.NOT_EXISTS_ANY_STATIONS_ERROR;
import static nextstep.error.ErrorCode.SECTION_MINIMUM_SIZE_ERROR;

@Embeddable
public class Sections {

    private static final int FIRST = 0;
    private static final int SECTION_MINIMUM_SIZE = 1;
    private static final int IS_NOT_REQUIRED_RE_ORDER_COUNT = 1;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addFirst(Section section) {
        if (!sections.isEmpty()) {
            throw new ValidationException(FIRST_SECTION_CREATE_ERROR);
        }
        sections.add(section);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        save(section);
    }

    public void remove(final Long stationId) {
        validateDelete(stationId);
        removeAndRePosition(stationId);
    }

    public Section findSectionHasUpStationEndPoint() {
        return sections.stream()
                .filter(s -> s.getUpStation().equals(getUpStationEndPoint()))
                .collect(Collectors.toList())
                .get(FIRST);
    }

    public Section findSectionByDownStation(final Station downStation) {
        return sections.stream()
                .filter(s -> s.getUpStation().equals(downStation))
                .collect(Collectors.toList())
                .get(FIRST);
    }

    private Station getUpStationEndPoint() {
        List<Station> downStations = findDownStations();
        return findUpStations().stream()
                .filter(us -> !downStations.contains(us))
                .findAny()
                .orElseThrow(NotFoundStationException::new);
    }

    private void save(final Section target) {
        validateSave(target);
        saveBetweenStations(target);
        sections.add(target);
    }

    private void validateSave(final Section target) {
        List<Station> stations = getAllStations();
        if (isAlreadyExistBothStation(stations, target)) {
            throw new ValidationException(ALREADY_EXISTS_STATIONS_ERROR);
        }
        if (isNotExistsAnyStation(stations, target)) {
            throw new ValidationException(NOT_EXISTS_ANY_STATIONS_ERROR);
        }
    }

    private void saveBetweenStations(Section target) {
        for (Section section : sections) {
            changeUpStation(section, target);
            changeDownStation(section, target);
        }
    }

    private boolean isNotExistsAnyStation(final List<Station> stations, final Section target) {
        return !stations.contains(target.getUpStation()) && !stations.contains(target.getDownStation());
    }

    private boolean isAlreadyExistBothStation(final List<Station> stations, final Section target) {
        return stations.contains(target.getUpStation()) && stations.contains(target.getDownStation());
    }

    private void changeUpStation(Section section, Section target) {
        if (isThereAnSameUpStationInTheSections(target)) {
            section.validateSectionDistance(target);
            section.changeUpStation(target.getDownStation());
        }
    }

    private void changeDownStation(Section section, Section target) {
        if (isThereAnSameDownStationInTheSections(target)) {
            section.validateSectionDistance(target);
            section.changeDownStation(target.getUpStation());
        }
    }

    private boolean isThereAnSameUpStationInTheSections(Section target) {
        return sections.stream()
                .anyMatch(s -> s.getUpStation().equals(target.getUpStation()));
    }

    private boolean isThereAnSameDownStationInTheSections(Section target) {
        return sections.stream()
                .anyMatch(s -> s.getDownStation().equals(target.getDownStation()));
    }

    private void removeAndRePosition(final Long stationId) {
        Optional<Section> up = findSectionByUpStationId(stationId);
        Optional<Section> down = findSectionByDownStationId(stationId);

        if(isMiddleSectionDeleteRequest(up, down)) {
            sections.add(createNewSectionByRePosition(up.get(), down.get()));
            sections.remove(up.get());
            sections.remove(down.get());
            return;
        }

        up.ifPresent(section -> sections.remove(section));
        down.ifPresent(section -> sections.remove(section));
    }

    private void validateDelete(final Long stationId) {
        validateNotExists(stationId);
        validateSectionMinimumSize();
    }

    private void validateNotExists(final Long downStationId) {
        if(isNotExists(downStationId)) {
            throw new NotFoundStationException();
        }
    }

    private boolean isNotExists(final Long id) {
        final List<Station> registeredDownStations = getRegisteredDownStation();
        final List<Station> registeredUpStations = getRegisteredUpStation();
        return !getIds(registeredDownStations).contains(id) && !getIds(registeredUpStations).contains(id);
    }

    private void validateSectionMinimumSize() {
        if (sections.size() <= SECTION_MINIMUM_SIZE) {
            throw new ValidationException(SECTION_MINIMUM_SIZE_ERROR);
        }
    }

    private Optional<Section> findSectionByUpStationId(final Long stationId) {
        return sections.stream()
                .filter(s -> {
                    Station upStation = s.getUpStation();
                    Long id = upStation.getId();
                    return id.equals(stationId);
                })
                .findFirst();
    }

    private Optional<Section> findSectionByDownStationId(final Long stationId) {
        return sections.stream()
                .filter(s -> {
                    Station downStation = s.getDownStation();
                    Long id = downStation.getId();
                    return id.equals(stationId);
                })
                .findFirst();
    }

    private boolean isMiddleSectionDeleteRequest(Optional<Section> up, Optional<Section> down) {
        return up.isPresent() && down.isPresent();
    }

    private Section createNewSectionByRePosition(Section up, Section down) {
        return Section.of(
                up.getLine(),
                down.getUpStation(),
                up.getDownStation(),
                up.getDistance() + down.getDistance()
        );
    }

    public Station getDownStationEndPoint() {
        List<Station> upStations = findUpStations();
        return findDownStations().stream()
                .filter(ds -> !upStations.contains(ds))
                .findAny()
                .orElseThrow(NotFoundStationException::new);
    }

    private List<Long> getIds(List<Station> registeredStations) {
        return registeredStations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public List<Station> getAllStations() {
        return Stream.concat(getRegisteredDownStation().stream(), getRegisteredUpStation().stream())
                .collect(Collectors.toList());
    }

    private List<Station> getRegisteredDownStation() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private List<Station> getRegisteredUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    private List<Station> findUpStations() {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private List<Station> findDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    public List<Station> createStations() {
        return isRequiredReOrdering(this)
                ? createStations(createReOrderedSections(this))
                : createStations(sections);
    }

    private static boolean isRequiredReOrdering(final Sections sections) {
        return sections.getSections().size() > IS_NOT_REQUIRED_RE_ORDER_COUNT;
    }

    private static List<Section> createReOrderedSections(final Sections sections) {
        List<Section> sectionStorage = new ArrayList<>();
        Section start = sections.findSectionHasUpStationEndPoint();
        sectionStorage.add(start);
        reOrderSections(sections, start, sectionStorage);
        return sectionStorage;
    }

    private static void reOrderSections(
            final Sections sections,
            final Section start,
            final List<Section> sectionStorage
    ) {
        Section findSection = sections.findSectionByDownStation(start.getDownStation());
        sectionStorage.add(findSection);
        if(sections.getDownStationEndPoint().equals(findSection.getDownStation())) {
            return;
        }

        reOrderSections(sections, findSection, sectionStorage);
    }

    private static List<Station> createStations(final List<Section> sections) {
        return Stream.concat(
                sections.stream().map(Section::getUpStation),
                sections.stream().map(Section::getDownStation)
        ).distinct().collect(Collectors.toList());
    }
}
