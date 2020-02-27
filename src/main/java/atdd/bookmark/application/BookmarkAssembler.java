package atdd.bookmark.application;

import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import atdd.bookmark.entity.Bookmark;
import atdd.path.application.dto.StationResponseView;
import atdd.path.domain.Station;

public class BookmarkAssembler {
  public static BookmarkSimpleResponseView EntityToDTO(Bookmark bookmark) {
    Station sourceStation = bookmark.getSourceStation();
    Station targetStation = bookmark.getTargetStation();
    if (targetStation == null) {
      return new BookmarkSimpleResponseView(bookmark.getId(),
          new StationResponseView(sourceStation.getId(), sourceStation.getName()));
    }
    return new BookmarkSimpleResponseView(bookmark.getId(),
        new StationResponseView(sourceStation.getId(), sourceStation.getName()),
        new StationResponseView(targetStation.getId(), targetStation.getName()));
  }
}
