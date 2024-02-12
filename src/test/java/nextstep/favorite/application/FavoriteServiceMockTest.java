package nextstep.favorite.application;

import nextstep.common.fixture.FavoriteFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteSaveException;
import nextstep.member.domain.LoginMember;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private LoginMember loginMember;
    private Long 강남역_Id;
    private Long 선릉역_Id;
    private Long memberId;
    private Long favoriteId;
    private Station 강남역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationProvider, pathService);
        loginMember = new LoginMember(memberId, "test@test.com");
        강남역_Id = 1L;
        선릉역_Id = 2L;
        memberId = 1L;
        favoriteId = 1L;
        강남역 = StationFactory.createStation(강남역_Id, "강남역");
        선릉역 = StationFactory.createStation(선릉역_Id, "선릉역");
    }

    @Test
    @DisplayName("즐겨찾기를 생성할 수 있다")
    void createFavoriteTest() {
        given(stationProvider.findById(강남역_Id)).willReturn(강남역);
        given(stationProvider.findById(선릉역_Id)).willReturn(선릉역);
        given(favoriteRepository.save(any())).willReturn(FavoriteFactory.createFavorite(favoriteId, memberId, 강남역, 선릉역));
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        final FavoriteResponse actual = favoriteService.createFavorite(loginMember, request);

        final FavoriteResponse expected = new FavoriteResponse(favoriteId, StationResponse.from(강남역), StationResponse.from(선릉역));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 경로는 즐겨찾기를 생성할 수 없다")
    void favoritePathNotValidTest() {
        given(pathService.isInvalidPath(any())).willReturn(true);
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(FavoriteSaveException.class)
                .hasMessageContaining("존재하지 않는 경로는 즐겨찾기에 추가할 수 없습니다.");
    }
}
