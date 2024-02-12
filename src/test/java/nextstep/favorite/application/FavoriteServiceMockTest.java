package nextstep.favorite.application;

import nextstep.common.fixture.FavoriteFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.station.application.StationProvider;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationProvider stationProvider;

    @Test
    @DisplayName("즐겨찾기를 생성할 수 있다")
    void createFavoriteTest() {
        final FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationProvider);

        final Long 강남역_Id = 1L;
        final Long 선릉역_Id = 2L;
        final Long memberId = 1L;
        final Long favoriteId = 1L;
        final Station 강남역 = StationFactory.createStation(강남역_Id, "강남역");
        final Station 선릉역 = StationFactory.createStation(선릉역_Id, "선릉역");
        given(stationProvider.findById(강남역_Id)).willReturn(강남역);
        given(stationProvider.findById(선릉역_Id)).willReturn(선릉역);
        given(favoriteRepository.save(any())).willReturn(FavoriteFactory.createFavorite(favoriteId, memberId, 강남역, 선릉역));
        final LoginMember loginMember = new LoginMember(memberId, "test@test.com");
        final FavoriteRequest request = new FavoriteRequest(강남역_Id, 선릉역_Id);

        final FavoriteResponse response = favoriteService.createFavorite(loginMember, request);

        assertSoftly(softly->{
            softly.assertThat(response.getId()).isEqualTo(favoriteId);
            softly.assertThat(response.getSource().getId()).isEqualTo(강남역_Id);
            softly.assertThat(response.getTarget().getId()).isEqualTo(선릉역_Id);
        });
    }
}
