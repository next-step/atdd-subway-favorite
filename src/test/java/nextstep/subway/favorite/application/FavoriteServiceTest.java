package nextstep.subway.favorite.application;

import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("회원별 즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
    }

    @Test
    @DisplayName("즐겨찾기 생성")
    void createFavorite() {
        //given
        Favorite mockFavorite = mock(Favorite.class);
        given(mockFavorite.getId()).willReturn(1L);
        given(favoriteRepository.save(any(Favorite.class))).willReturn(mockFavorite);
        //when
        Long savedId = favoriteService.createFavorite(1L, new FavoriteRequest(1L, 2L));

        //then
        assertThat(savedId).isEqualTo(mockFavorite.getId());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        //given
        Favorite favorite = reflectionFavorite(1L, 1L, 1L, 2L);
        given(favoriteRepository.findById(1L))
                .willReturn(Optional.of(favorite));

        //when
        favoriteService.deleteFavorite(1L, 1L);

        //then
        verify(favoriteRepository).delete(favorite);
    }

    @Test
    @DisplayName("본인 즐겨찾기가 아닌 즐겨찾기 삭제 요청시 권한 에러 발생")
    void deleteFavoriteWithNoPermission() {
        //given
        Favorite favorite = reflectionFavorite(1L, 2L, 1L, 2L);
        given(favoriteRepository.findById(1L))
                .willReturn(Optional.of(favorite));
        //when
        assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
                //then
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("즐겨찾기 목록 찾기")
    void findFavorite() {
        //given
        long memberId = 1L;
        given(favoriteRepository.findAllByMemberId(memberId)).willReturn(Lists.list(reflectionFavorite(1L, memberId, 1L, 2L), reflectionFavorite(2L, memberId, 2L, 3L)));
        given(stationRepository.findAllById(anyIterable())).willReturn(
                Lists.list(
                        reflectionStation(1L, "강남역"),
                        reflectionStation(2L, "양재역"),
                        reflectionStation(3L, "양재시민의숲역")
                )
        );

        //when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(1L);

        //then
        assertThat(favorites).isNotNull()
                .hasSize(2)
                .extracting(FavoriteResponse::getId)
                .containsExactly(1L, 2L);
    }

    private Favorite reflectionFavorite(long id, long memberId, long source, long target) {
        Favorite favorite = new Favorite(memberId, source, target);
        ReflectionTestUtils.setField(favorite, "id", id);
        return favorite;
    }

    private Station reflectionStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}