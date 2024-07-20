package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import nextstep.subway.station.domain.Station;

public class Path {
  private final List<Station> stations;
  @Getter private final long totalDistance;

  private Path(List<Station> stations, long totalDistance) {
    this.stations = stations;
    this.totalDistance = totalDistance;
  }

  public static Path empty() {
    return new Path(Collections.emptyList(), 0);
  }

  public static Path of(List<Station> stations, long totalDistance) {
    return new Path(stations, totalDistance);
  }

  public List<Station> getStations() {
    return Collections.unmodifiableList(stations);
  }
}
