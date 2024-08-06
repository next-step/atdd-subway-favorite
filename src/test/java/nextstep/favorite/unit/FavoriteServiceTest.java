package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @DisplayName("즐겨찾기 추가 함수는, 시작역과 종료역을 입력받아 즐겨찾기에 추가하고 추가된 즐겨찾기 정보를 반환한다.")
    @Test
    void createFavoriteTest() {
        // given
        Station 강남역 = Station.of(1L, "강남역");
        Station 양재역 = Station.of(2L, "양재역");
        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(양재역.getId())).thenReturn(양재역);
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService);

        // when
        favoriteService.createFavorite("admin@email.com", favoriteRequest);

        // then
    }

    @DisplayName("즐겨찾기 추가 함수는, 연결되지 않은 역을 추가하려고 하는 경우 NotConnectedStationsException이 발생한다.")
    @Test
    void createFavoriteNotConnectedStationsTest() {
        // given
        // when
        // then
    }

    @DisplayName("즐겨찾기 추가 함수는, 구간에 역을 추가하지 않은 경우 NotAddedStationsToSectionException이 발생한다.")
    @Test
    void createFavoriteNotAddedStationsToSectionTest() {
        // given
        // when
        // then
    }
}
