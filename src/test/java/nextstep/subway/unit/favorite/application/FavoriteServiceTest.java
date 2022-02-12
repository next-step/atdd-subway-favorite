package nextstep.subway.unit.favorite.application;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.common.domain.model.exception.EntityNotFoundException;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.repository.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.station.application.StationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteService 테스트")
public class FavoriteServiceTest {
    private static final Favorite FAVORITE = new Favorite(1L, 1L, 1L, 2L);
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @InjectMocks
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기 추가 - 성공")
    @Test
    public void addFavorite() {
        // Given
        FavoriteRequest request = new FavoriteRequest(FAVORITE.getSourceId(), FAVORITE.getTargetId());

        doNothing().when(memberService)
                   .verifyExists(anyLong());
        doNothing().when(stationService)
                   .verifyExists(anyLong());
        when(favoriteRepository.save(any()))
            .thenReturn(FAVORITE);

        // When
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(FAVORITE.getMemberId(), request);

        // Then
        assertThat(favoriteResponse.getId()).isEqualTo(FAVORITE.getId());
        assertThat(favoriteResponse.getSource()).isEqualTo(FAVORITE.getSourceId());
        assertThat(favoriteResponse.getTarget()).isEqualTo(FAVORITE.getTargetId());
    }
}
