package atdd.bookmark.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import atdd.bookmark.application.dto.BookmarkRequestView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import atdd.bookmark.entity.Bookmark;
import atdd.bookmark.repository.BookmarkRepository;
import atdd.path.repository.StationRepository;
import atdd.user.application.exception.UnauthorizedException;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;

@SpringBootTest
public class BookmarkServiceTest {

  @MockBean
  private StationRepository stationRepository;

  @MockBean
  private BookmarkRepository bookmarkRepository;

  private BookmarkService bookmarkService;

  @BeforeEach
  void setUp() {
    this.bookmarkService = new BookmarkService(bookmarkRepository, stationRepository);
  }

  @Test
  void addStationBookmark() {
    given(stationRepository.findById(any(Long.class)))
      .willReturn(Optional.of(TEST_STATION));

    Bookmark bookmark = new Bookmark(1L, TEST_STATION, null);
    given(bookmarkRepository.save(any(Bookmark.class)))
        .willReturn(bookmark);
    
    BookmarkRequestView bookmarkRequestView = new BookmarkRequestView(TEST_STATION.getId());

    BookmarkSimpleResponseView result = bookmarkService.addStationBookmark(1L, bookmarkRequestView.getSourceStationID());

    assertThat(result.getSourceStation()).isNotNull();
  }

  @Test 
  void retrieveAllBookmarks() {
  }

  @Test
  void deleteBookmark() {
    Long testUserID = 1L;
    Bookmark bookmark = new Bookmark(1L, testUserID, TEST_STATION, null);

    given(bookmarkRepository.findById(1L))
      .willReturn(Optional.of(bookmark));
    
    doNothing().when(bookmarkRepository).deleteById(isA(Long.class));

    bookmarkService.deleteBookmark(testUserID, bookmark.getId());
  }

  @Test
  void deleteBookmarkNotAuthorized() {
    Long testUserID = 1L;
    Long bookmarkOwnerID = 2L;
    Bookmark bookmark = new Bookmark(1L, bookmarkOwnerID, TEST_STATION, null);

    given(bookmarkRepository.findById(1L))
      .willReturn(Optional.of(bookmark));
    
    try {
      bookmarkService.deleteBookmark(testUserID, bookmark.getId());
    } catch (UnauthorizedException e) {
      return;
    }

    fail("bookmark delete 권한 검사 실패");
  }
}
