package atdd.bookmark.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import atdd.auth.LoginUser;
import atdd.bookmark.application.dto.BookmarkRequestView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import atdd.bookmark.entity.Bookmark;
import atdd.bookmark.repository.BookmarkRepository;
import atdd.path.application.dto.StationResponseView;
import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Station;
import atdd.path.repository.StationRepository;
import atdd.user.application.dto.UserResponseView;

@Controller
public class BookmarkController {
  private BookmarkRepository bookmarkRepository;
  private StationRepository stationRepository;

  public BookmarkController(BookmarkRepository bookmarkRepository, StationRepository stationRepository) {
    this.bookmarkRepository = bookmarkRepository;
    this.stationRepository = stationRepository;
  }


  @PostMapping("/bookmark")
  public ResponseEntity addBookmark(@LoginUser UserResponseView userResponseView, @RequestBody BookmarkRequestView bookmarkRequestView) {
    Station sourceStation = stationRepository
      .findById(
          bookmarkRequestView.getSourceStationID())
      .orElseThrow(NoDataException::new);

        
    Bookmark created = bookmarkRepository.save(
      Bookmark.builder()
        .userID(userResponseView.getId())
        .sourceStation(sourceStation)
        .build());

    return ResponseEntity
      .created(URI.create("/bookmark/" + created.getId()))
      .body(
          new BookmarkSimpleResponseView(
            created.getId(),
            new StationResponseView(
              created.getSourceStation().getId(),
              created.getSourceStation().getName()
              )
            ));
  }

  @GetMapping("/bookmark/station")
  public ResponseEntity retriveAllStationBookmarks(@LoginUser UserResponseView userResponseView) {
    return null;
  }

  @GetMapping("/bookmark/station/{id}")
  public ResponseEntity retriveStationBookmarks(@LoginUser UserResponseView userResponseView, @PathVariable Long id) {
    return null;
  }

  @DeleteMapping("/bookmark/station/{id}")
  public ResponseEntity removeStationBookmarks(@LoginUser UserResponseView userResponseView, @PathVariable Long id) {
    return null;
  }

}
