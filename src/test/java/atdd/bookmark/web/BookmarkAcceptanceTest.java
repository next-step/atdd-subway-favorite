package atdd.bookmark.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import atdd.AbstractAcceptanceTest;
import atdd.bookmark.dto.StationBookmarkResponseView;
import atdd.bookmark.dto.StationBookmarkSimpleResponseView;
import atdd.path.web.LineHttpTest;
import atdd.path.web.StationHttpTest;
import atdd.user.web.UserManageHttpTest;

import static atdd.path.TestConstant.*;
import static atdd.user.TestConstant.*;

import java.util.List;

import static atdd.bookmark.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BookmarkAcceptanceTest extends AbstractAcceptanceTest {
  private StationHttpTest stationHttpTest;
  private LineHttpTest lineHttpTest;
  private UserManageHttpTest userManageHttpTest;
  private BookmarkHttpTest bookmarkHttpTest;
  private List<Long> stations;

  private String authToken;

  @BeforeEach
  void setUp() {
    this.stationHttpTest = new StationHttpTest(webTestClient);
    this.lineHttpTest = new LineHttpTest(webTestClient);
    this.userManageHttpTest = new UserManageHttpTest(webTestClient);

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
  public void addStationBookmark() {
    //when
    StationBookmarkSimpleResponseView stationBookmarkResponseView = bookmarkHttpTest.addStationBookmark(stations.get(0)).getResponseBody();

    //then
    assertThat(stationBookmarkResponseView.getId()).isNotNull();
    assertThat(stationBookmarkResponseView.getStation()).isNotNull();
  }

  @Test
  public void getAllStationBookmark() {
    //Given
    StationBookmarkSimpleResponseView bookmark1 = bookmarkHttpTest.addStationBookmark(stations.get(0)).getResponseBody();
    StationBookmarkSimpleResponseView bookmark2 = bookmarkHttpTest.addStationBookmark(stations.get(1)).getResponseBody();

    //when
    StationBookmarkResponseView stations =  bookmarkHttpTest.getBookmarks().getResponseBody();

    //then
    assertThat(stations.getStations().size()).isEqualTo(2);
    assertThat(stations.getStations().get(0)
        .getId()).isEqualTo(bookmark1.getId());
    assertThat(stations.getStations().get(1)
        .getId()).isEqualTo(bookmark2.getId());
  }

  @Test
  public void deleteStationBookmark() {
    //Given
    StationBookmarkSimpleResponseView bookmark1 = bookmarkHttpTest.addStationBookmark(stations.get(0)).getResponseBody();

    //when
    bookmarkHttpTest.deleteBookmarks(bookmark1.getId());

    //then
    StationBookmarkResponseView stations =  bookmarkHttpTest.getBookmarks().getResponseBody();
    assertThat(stations.getStations().size()).isEqualTo(0);
  }
}
