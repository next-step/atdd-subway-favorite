package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.domain.StationRepository;

@DisplayName("지하철 즐겨찾기 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    private static final Long SOURCE_STATION_ID = 1L;
    private static final Long TARGET_STATION_ID = 2L;
    private static final Long LOGINED_MEMBER_ID = 1L;
    private static final Long FAVORITE_ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;
    private Favorite expectedFavorite;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        expectedFavorite = new Favorite(FAVORITE_ID, LOGINED_MEMBER_ID, SOURCE_STATION_ID, TARGET_STATION_ID);
        favoriteRequest = new FavoriteRequest(SOURCE_STATION_ID, TARGET_STATION_ID);
    }

    @DisplayName("하나의 멤버에 새로운 즐겨찾기를 등록한다.")
    @Test
    void 즐겨찾기를_등록한다() {
        // given: Repository의 반환값을 명시한다.
        when(favoriteRepository.save(any())).thenReturn(expectedFavorite);

        // when: 즐겨찾기를 반환한다.
        Favorite actualFavorite = favoriteService.createFavorite(LOGINED_MEMBER_ID, favoriteRequest);

        // then: 즐겨찾기의 기댓값과 비교한다.
        assertThat(actualFavorite).isEqualTo(expectedFavorite);
    }
}
