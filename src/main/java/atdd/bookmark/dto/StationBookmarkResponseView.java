package atdd.bookmark.dto;

import java.util.List;

public class StationBookmarkResponseView {
  private List<StationBookmarkSimpleResponseView> stations;

  public StationBookmarkResponseView(List<StationBookmarkSimpleResponseView> stations) {
    this.stations = stations;
  }

  public List<StationBookmarkSimpleResponseView> getStations() {
    return stations;
  }

}
