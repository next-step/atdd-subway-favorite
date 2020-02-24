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
import atdd.bookmark.application.BookmarkService;
import atdd.bookmark.application.dto.BookmarkRequestView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
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

  @DeleteMapping("/bookmark/{id}")
  public ResponseEntity removeStationBookmarks(@LoginUser UserResponseView userResponseView, @PathVariable(name = "id") Long bookmarkID) {
    bookmarkService.deleteBookmark(
        userResponseView.getId(),bookmarkID);
    return ResponseEntity.noContent().build();
  }

}
