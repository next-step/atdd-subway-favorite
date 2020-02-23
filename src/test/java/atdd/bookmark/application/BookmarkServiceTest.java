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

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
}
