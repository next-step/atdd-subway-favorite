package nextstep.subway.line.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.CannotAddLineSectionException;
import nextstep.subway.line.exception.CannotRemoveLastLineSectionException;
import nextstep.subway.line.exception.LineSectionAlreadyExistsException;
import nextstep.subway.line.exception.StationNotFoundInLineException;
import nextstep.subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineSections {
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "line_id")
  private final List<LineSection> sections = new ArrayList<>();

  @Builder
  public LineSections(List<LineSection> lineSections) {
    this.sections.addAll(lineSections);
  }

  public LineSections(LineSection... lineSections) {
    this.sections.addAll(Arrays.asList(lineSections));
  }

  public LineSections(Station upStation, Station downStation, int distance) {
    this(LineSection.of(upStation, downStation, distance));
  }

  public static LineSections of(Station upStation, Station downStation, int distance) {
    return new LineSections(upStation, downStation, distance);
  }

  public int size() {
    return sections.size();
  }

  public boolean isEmpty() {
    return sections.isEmpty();
  }

  public LineSection getFirst() {
    return sections.get(0);
  }

  public LineSection getLast() {
    return sections.get(sections.size() - 1);
  }

  public void add(LineSection lineSection) {
    validateAdd(lineSection);
    if (isEmpty()) {
      sections.add(lineSection);
      return;
    }
    if (isPrepend(lineSection)) {
      sections.add(0, lineSection);
      return;
    }
    if (isAppend(lineSection)) {
      sections.add(lineSection);
      return;
    }
    if (insertUp(lineSection)) {
      return;
    }
    if (insertDown(lineSection)) {
      return;
    }
    throw new CannotAddLineSectionException();
  }

  private boolean insertUp(LineSection lineSection) {
    OptionalInt optionalIndex = indexOfSplitUp(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private boolean insertDown(LineSection lineSection) {
    OptionalInt optionalIndex = indexOfSplitDown(lineSection);
    if (optionalIndex.isPresent()) {
      insert(lineSection, optionalIndex.getAsInt());
      return true;
    }
    return false;
  }

  private void insert(LineSection lineSection, int index) {
    LineSection splitTargetSection = sections.remove(index);
    List<LineSection> splitSections = splitTargetSection.split(lineSection);
    sections.addAll(index, splitSections);
  }

  private OptionalInt indexOfSplitUp(LineSection lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitUp(lineSection))
        .findFirst();
  }

  private OptionalInt indexOfSplitDown(LineSection lineSection) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).canSplitDown(lineSection))
        .findFirst();
  }

  private boolean isPrepend(LineSection lineSection) {
    return getFirst().canPrepend(lineSection);
  }

  private boolean isAppend(LineSection lineSection) {
    return getLast().canAppend(lineSection);
  }

  private void validateAdd(LineSection lineSection) {
    if (containsBothStations(lineSection)) {
      throw new LineSectionAlreadyExistsException();
    }
  }

  private boolean containsBothStations(LineSection lineSection) {
    List<Station> stations = getStations();
    return stations.stream().anyMatch(it -> it.isSame(lineSection.getUpStation()))
        && stations.stream().anyMatch(it -> it.isSame(lineSection.getDownStation()));
  }

  public void addAll(LineSections lineSections) {
    lineSections.sections.forEach(this::add);
  }

  public List<Station> getStations() {
    if (sections.isEmpty()) {
      return Collections.emptyList();
    }
    List<Station> stations =
        sections.stream().map(LineSection::getUpStation).collect(Collectors.toList());
    stations.add(getLast().getDownStation());
    return Collections.unmodifiableList(stations);
  }

  public void remove(Station station) {
    validateRemove(station);
    if (removeTerminal(station)) {
      return;
    }
    if (removeMiddle(station)) {
      return;
    }
    throw new IllegalArgumentException("역 #" + station.getId() + " 를 제거할 수 없습니다.");
  }

  private boolean removeMiddle(Station station) {
    OptionalInt optionalIndex = indexOfSectionContaining(station);
    if (optionalIndex.isEmpty()) {
      return false;
    }
    int index = optionalIndex.getAsInt();
    LineSection upSection = sections.remove(index);
    LineSection downSection = sections.remove(index);
    LineSection mergedSection = upSection.merge(downSection);
    sections.add(index, mergedSection);
    return true;
  }

  private OptionalInt indexOfSectionContaining(Station station) {
    return IntStream.range(0, sections.size())
        .filter(it -> sections.get(it).contains(station))
        .findFirst();
  }

  private boolean removeTerminal(Station station) {
    if (getFirst().getUpStation().isSame(station)) {
      sections.remove(0);
      return true;
    }
    if (getLast().getDownStation().isSame(station)) {
      sections.remove(sections.size() - 1);
      return true;
    }
    return false;
  }

  private void validateRemove(Station station) {
    if (!getStations().contains(station)) {
      throw new StationNotFoundInLineException(station.getId());
    }
    if (size() <= 1) {
      throw new CannotRemoveLastLineSectionException();
    }
  }
}
