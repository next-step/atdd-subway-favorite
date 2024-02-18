package nextstep.favorite.application;

import nextstep.common.fixture.FavoriteFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteNotExistException;
import nextstep.favorite.exception.FavoriteSaveException;
import nextstep.auth.ui.UserPrincipal;
import nextstep.path.application.PathService;
import nextstep.station.application.StationProvider;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationProvider stationProvider;
    @Mock
    private PathService pathService;

    private FavoriteService favoriteService;
    private UserPrincipal userPrincipal;
    private final Long 강남역_Id = 1L;
    private final Long 선릉역_Id = 2L;
    private final Long memberId = 1L;
    private Station 강남역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        userPrincipal = new UserPrincipal(memberId, "test@test.com");
        강남역 = StationFactory.createStation(강남역_Id, "강남역");
        선릉역 = StationFactory.createStation(선릉역_Id, "선릉역");
        favoriteService = new FavoriteService(favoriteRepository, stationProvider, pathService);
    }

    @Test
    @DisplayName("즐겨찾기를 생성할 수 있다")
    void createFavoriteTest() {
        final Long favoriteId = 1L;

        given(stationProvider.findById(강남역_Id)).willReturn(강남역);
        given(stationProvider.findById(선릉역_Id)).willReturn(선릉역);
        given(favoriteRepository.save(any())).willReturn(FavoriteFactory.createFavorite(favoriteId, memberId, 강남역, 선릉역));
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        final FavoriteResponse actual = favoriteService.createFavorite(userPrincipal, request);

        final FavoriteResponse expected = new FavoriteResponse(favoriteId, StationResponse.from(강남역), StationResponse.from(선릉역));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경로는 즐겨찾기를 생성할 수 없다")
    void favoriteTargetSourceSameTest() {
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 강남역_Id);

        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("출발역과 도착역이 같은 경로는 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 경로는 즐겨찾기를 생성할 수 없다")
    void favoritePathNotValidTest() {
        given(pathService.isInvalidPath(any())).willReturn(true);
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("존재하지 않는 경로는 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 등록한 즐겨찾기 경로는 다시 등록할 수 없다")
    void favoritePathDuplicateTest() {
        given(favoriteRepository.existsByStations(memberId, 강남역_Id, 선릉역_Id)).willReturn(true);

        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("이미 등록된 즐겨찾기 경로입니다.");
    }

    @Test
    @DisplayName("즐겨찾기를 삭제할 수 있다")
    void deleteFavoriteTest() {
        final Long favoriteId = 1L;

        given(favoriteRepository.findByIdAndMember(favoriteId, memberId)).willReturn(Optional.of(FavoriteFactory.createFavorite(favoriteId, memberId, 강남역, 선릉역)));

        assertDoesNotThrow(() -> favoriteService.deleteFavorite(userPrincipal, favoriteId));
    }

    @Test
    @DisplayName("존재하지 않는 즐겨찾기는 삭제할 수 없다")
    void deleteFavoriteNotExistTest() {
        final Long favoriteId = 1L;

        given(favoriteRepository.findByIdAndMember(favoriteId, memberId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.deleteFavorite(userPrincipal, favoriteId))
                .isInstanceOf(FavoriteNotExistException.class);
    }
}
