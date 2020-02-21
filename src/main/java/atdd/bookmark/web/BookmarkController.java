package atdd.bookmark.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import atdd.auth.LoginUser;
import atdd.bookmark.application.dto.StationBookmarkRequestView;
import atdd.user.application.dto.UserResponseView;

@Controller
public class BookmarkController {
  @PostMapping("/bookmark/station")
  public ResponseEntity addStationBookmark(@LoginUser UserResponseView userResponseView, @RequestBody StationBookmarkRequestView stationBookmarkRequestView) {
    return null;
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
