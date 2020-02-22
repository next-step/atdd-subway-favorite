package atdd.bookmark.application.dto;

import atdd.path.application.dto.StationResponseView;

public class StationBookmarkSimpleResponseView {
  private Long id;
  private StationResponseView station;

  public StationBookmarkSimpleResponseView(Long id, StationResponseView station) {
    this.id = id;
    this.station = station;
  }

  public Long getId() {
    return id;
  }

  public StationResponseView getStation() {
    return station;
  }
}
