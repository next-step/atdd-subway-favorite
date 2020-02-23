package atdd.bookmark.application;

import org.springframework.stereotype.Service;

import atdd.bookmark.application.dto.BookmarkRequestView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import atdd.bookmark.entity.Bookmark;
import atdd.bookmark.repository.BookmarkRepository;
import atdd.path.application.dto.StationResponseView;
import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Station;
import atdd.path.repository.StationRepository;

@Service
public class BookmarkService {
  private BookmarkRepository bookmarkRepository;
  private StationRepository stationRepository;

  public BookmarkService(BookmarkRepository bookmarkRepository, StationRepository stationRepository) {
    this.bookmarkRepository = bookmarkRepository;
    this.stationRepository = stationRepository;
  }

  public BookmarkSimpleResponseView addStationBookmark(Long userID, BookmarkRequestView bookmarkRequestView) {
    Station sourceStation = stationRepository.findById(
        bookmarkRequestView.getSourceStationID()
        ).orElseThrow(NoDataException::new);

    Bookmark created = bookmarkRepository.save(
        new Bookmark(userID, sourceStation)
        );

    return new BookmarkSimpleResponseView(
        created.getId(),
        new StationResponseView(
          sourceStation.getId(), sourceStation.getName()
          )
        );
  }


}
