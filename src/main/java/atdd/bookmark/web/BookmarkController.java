package atdd.bookmark.web;

import java.net.URI;

import javax.activation.UnsupportedDataTypeException;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import atdd.auth.LoginUser;
import atdd.bookmark.application.BookmarkService;
import atdd.bookmark.application.dto.BookmarkRequestView;
import atdd.bookmark.application.dto.BookmarkResponseView;
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
  private BookmarkService bookmarkService;

  public BookmarkController(BookmarkService bookmarkService) {
    this.bookmarkService = bookmarkService;
  }

  @PostMapping("/bookmark")
  public ResponseEntity addBookmark(@LoginUser UserResponseView userResponseView, @RequestBody BookmarkRequestView bookmarkRequestView) {
    if (bookmarkRequestView.isStationBookmark()) {
      BookmarkSimpleResponseView result = bookmarkService.addStationBookmark(
          userResponseView.getId(),
          bookmarkRequestView.getSourceStationID());

      return ResponseEntity
        .created(URI.create("/bookmark/" + result.getId()))
        .body(result);
    }

    return null;
  }

  @GetMapping("/bookmark")
  public ResponseEntity retriveAllBookmarks(@LoginUser UserResponseView userResponseView) {
    return ResponseEntity.ok(
      bookmarkService.retrieveAllBookmarks(userResponseView.getId())
        );
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
