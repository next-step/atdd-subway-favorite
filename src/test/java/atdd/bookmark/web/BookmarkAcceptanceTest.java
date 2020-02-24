package atdd.bookmark.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import atdd.AbstractAcceptanceTest;
import atdd.bookmark.application.dto.BookmarkResponseView;
import atdd.bookmark.application.dto.BookmarkSimpleResponseView;
import atdd.path.application.dto.PathResponseView;
import atdd.path.web.GraphHttpTest;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.web.UserManageHttpTest;

import static atdd.path.TestConstant.*;
import static atdd.user.TestConstant.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookmarkAcceptanceTest extends AbstractAcceptanceTest {
  private StationHttpTest stationHttpTest;
  private LineHttpTest lineHttpTest;
  private GraphHttpTest graphHttpTest;
  private UserManageHttpTest userManageHttpTest;
  private BookmarkHttpTest bookmarkHttpTest;
  private List<Long> stations;

  private String authToken;

  @BeforeEach
  void setUp() {
    this.stationHttpTest = new StationHttpTest(webTestClient);
    this.lineHttpTest = new LineHttpTest(webTestClient);
    this.graphHttpTest = new GraphHttpTest(webTestClient);
    this.userManageHttpTest = new UserManageHttpTest(webTestClient);
    this.stations = new ArrayList<>();

    userManageHttpTest.createNewUser(
        USER_1_EMAIL, 
        USER_1_NAME,
        USER_1_PASSWORD
        );
    this.authToken = userManageHttpTest.loginUser(USER_1_EMAIL, USER_1_PASSWORD)
      .getResponseBody()
      .toHeaderString();
    this.bookmarkHttpTest = new BookmarkHttpTest(webTestClient, authToken);

    this.stations.add(
        stationHttpTest.createStation(STATION_NAME));
    this.stations.add(
        stationHttpTest.createStation(STATION_NAME_2));
    this.stations.add(
        stationHttpTest.createStation(STATION_NAME_3));
    this.stations.add(
        stationHttpTest.createStation(STATION_NAME_4));

    Long lineId = lineHttpTest.createLine(LINE_NAME);
    lineHttpTest.createEdgeRequest(lineId, stations.get(0), stations.get(1));
    lineHttpTest.createEdgeRequest(lineId, stations.get(1), stations.get(2));
    lineHttpTest.createEdgeRequest(lineId, stations.get(2), stations.get(3));
  }

  @Test
  public void addBookmarkStation() {
    //when
    BookmarkSimpleResponseView stationBookmarkResponseView = bookmarkHttpTest.addBookmark(stations.get(0)).getResponseBody();

    //then
    assertThat(stationBookmarkResponseView.getId()).isNotNull();
    assertThat(stationBookmarkResponseView.getSourceStation().getName()).isNotNull();
  }

  @Test
  public void getAllBookmarkStation() {
    //Given
    BookmarkSimpleResponseView bookmark1 = bookmarkHttpTest.addBookmark(stations.get(0)).getResponseBody();
    BookmarkSimpleResponseView bookmark2 = bookmarkHttpTest.addBookmark(stations.get(1)).getResponseBody();

    //when
    BookmarkResponseView stations =  bookmarkHttpTest.getBookmarks().getResponseBody();

    //then
    assertThat(stations.getBookmarkSize()).isEqualTo(2);
    assertThat(stations.getBookmarks().get(0)
        .getId()).isEqualTo(bookmark1.getId());
    assertThat(stations.getBookmarks().get(1)
        .getId()).isEqualTo(bookmark2.getId());
  }

  @Test
  public void deleteBookmarkStation() {
    //Given
    BookmarkSimpleResponseView bookmark1 = bookmarkHttpTest.addBookmark(stations.get(0)).getResponseBody();

    //when
    bookmarkHttpTest.deleteBookmarks(bookmark1.getId());

    //then
    BookmarkResponseView stations =  bookmarkHttpTest.getBookmarks().getResponseBody();
    assertThat(stations.getBookmarks().size()).isEqualTo(0);
  }

  @Test
  public void addBookmarkPath() {
    //Then
    PathResponseView pathResponseView = graphHttpTest.findPath(
        stations.get(0), stations.get(2)
        ).getResponseBody();

    //When
    BookmarkSimpleResponseView bookmarkResponseView =  bookmarkHttpTest.addBookmark(
        pathResponseView.getStartStationId(),
        pathResponseView.getEndStationId()
        ).getResponseBody();

    assertThat(bookmarkResponseView.getId()).isNotNull();
    assertThat(bookmarkResponseView.getSourceStation()).isEqualTo(stations.get(0));
    assertThat(bookmarkResponseView.getTargetStation()).isEqualTo(stations.get(1));
  }

  @Test 
  public void retriveAllPathBookmarks() {
    //Given
    BookmarkSimpleResponseView bookmarkResponseView =  bookmarkHttpTest.addBookmark(
        stations.get(0),
        stations.get(2),
        ).getResponseBody();
    BookmarkSimpleResponseView bookmarkResponseView2 =  bookmarkHttpTest.addBookmark(
        stations.get(1),
        stations.get(3),
        ).getResponseBody();

    //When
    BookmarkResponseView bookmarks =  bookmarkHttpTest.getBookmarks().getResponseBody();

    //then
    assertThat(bookmarks.getBookmarkSize()).isEqualTo(2);
    assertThat(
        bookmarks.getBookmarks()
        .get(0).getId()
        ).isEqualTo(bookmarkResponseView.getId());
    assertThat(
        bookmarks.getBookmarks()
        .get(1).getId()
        ).isEqualTo(bookmarkResponseView2.getId());
  }
}
