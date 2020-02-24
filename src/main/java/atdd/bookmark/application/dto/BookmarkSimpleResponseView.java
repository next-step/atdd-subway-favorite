package atdd.bookmark.application.dto;

import atdd.path.application.dto.StationResponseView;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BookmarkSimpleResponseView {
  private Long id;
  private StationResponseView sourceStation;
  private StationResponseView targetStation;

  public BookmarkSimpleResponseView() {
  }

  public BookmarkSimpleResponseView(Long id, StationResponseView station) {
    this.id = id;
    this.sourceStation = station;
  }

  public BookmarkSimpleResponseView(Long id, StationResponseView sourceStation, StationResponseView targetStation){
    this.id = id;
    this.sourceStation = sourceStation;
    this.targetStation = targetStation;
  }

  public Long getId() {
    return id;
  }

  public StationResponseView getSourceStation() {
    return sourceStation;
  }

  public StationResponseView getTargetStation() {
    return targetStation;
  }
}
