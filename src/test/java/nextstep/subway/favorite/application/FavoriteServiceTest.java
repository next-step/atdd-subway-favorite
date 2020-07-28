package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("지하철 즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private static final long SOURCE = 1L;
    private static final long TARGET = 2L;
    public static final long MEMBER_ID = 1L;
    public static final long ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;
    private Favorite expected;
    private FavoriteRequest request;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        expected = new Favorite(ID, MEMBER_ID, SOURCE, TARGET);
        request = new FavoriteRequest(SOURCE, TARGET);
    }

    @Test
    void create() {
        // given
        when(favoriteRepository.save(any())).thenReturn(expected);

        // when
        Favorite actual = favoriteService.createFavorite(MEMBER_ID, request);

        // then
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void delete() {
        // given
        when(favoriteRepository.findByIdAndMemberID(MEMBER_ID, ID))
                .thenReturn(Optional.ofNullable(expected));

        // when
        favoriteService.deleteFavorite(MEMBER_ID, SOURCE);

        // then
        verify(favoriteRepository).findByIdAndMemberID(MEMBER_ID, SOURCE);
    }

    @Test
    void deleteWhenNotFound() {
        // given
        when(favoriteRepository.findByIdAndMemberID(MEMBER_ID, ID))
                .thenThrow(FavoriteNotFoundException.class);

        // when
        assertThatExceptionOfType(FavoriteNotFoundException.class)
                .isThrownBy(() -> favoriteService.deleteFavorite(MEMBER_ID, SOURCE));
    }

}
