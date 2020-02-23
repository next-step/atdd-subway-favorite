package atdd.bookmark.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BookmarkRequestView {
  Long sourceStationID;
  Long targetStationID;

  public BookmarkRequestView(Long sourceStationID) {
    this.sourceStationID = sourceStationID;
  }

  public BookmarkRequestView(Long sourceStationID, Long targetStationID) {
    this.sourceStationID = sourceStationID;
    this.targetStationID = targetStationID;
  }

  public Long getSourceStationID() {
    return sourceStationID;
  }

  public Long getTargetStationID() {
    return targetStationID;
  }

  public Boolean isStationBookmark() {
    return sourceStationID != null && targetStationID == null;
  }

  public Boolean isPathBookmark() {
    return sourceStationID != null && targetStationID != null;
  }
}
