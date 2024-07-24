package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineSection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  private int distance;

  @Builder
  public LineSection(Long id, Station upStation, Station downStation, int distance) {
    this.id = id;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public LineSection(Station upStation, Station downStation, int distance) {
    this(null, upStation, downStation, distance);
  }

  public static LineSection of(Station upStation, Station downStation, int distance) {
    return new LineSection(upStation, downStation, distance);
  }

  public boolean canPrepend(LineSection lineSection) {
    return upStation.isSame(lineSection.downStation);
  }

  public boolean canAppend(LineSection lineSection) {
    return downStation.isSame(lineSection.upStation);
  }

  public boolean canSplitUp(LineSection lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    return upStation.isSame(lineSection.getUpStation());
  }

  public boolean canSplitDown(LineSection lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    return downStation.isSame(lineSection.getDownStation());
  }

  public boolean isSame(LineSection lineSection) {
    return upStation.isSame(lineSection.getUpStation())
        && downStation.isSame(lineSection.getDownStation())
        && distance == lineSection.getDistance();
  }

  public List<LineSection> split(LineSection lineSection) {
    if (canSplitUp(lineSection)) {
      return List.of(
          LineSection.of(upStation, lineSection.getDownStation(), lineSection.distance),
          LineSection.of(
              lineSection.getDownStation(), downStation, distance - lineSection.distance));
    }
    if (canSplitDown(lineSection)) {
      return List.of(
          LineSection.of(upStation, lineSection.getUpStation(), distance - lineSection.distance),
          LineSection.of(lineSection.getUpStation(), downStation, lineSection.distance));
    }
    throw new IllegalArgumentException("LineSection#split 가 가능하지 않습니다.");
  }

  public boolean contains(Station station) {
    return upStation.isSame(station) || downStation.isSame(station);
  }

  public LineSection merge(LineSection lineSection) {
    if (canAppend(lineSection)) {
      return LineSection.of(
          upStation, lineSection.getDownStation(), distance + lineSection.distance);
    }
    if (canPrepend(lineSection)) {
      return LineSection.of(
          lineSection.getUpStation(), downStation, distance + lineSection.distance);
    }
    throw new IllegalArgumentException("LineSection#merge 가 가능하지 않습니다.");
  }
}
