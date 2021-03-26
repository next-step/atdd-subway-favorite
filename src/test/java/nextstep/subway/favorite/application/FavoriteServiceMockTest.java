package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.favorite.common.FavoriteConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 서초역;
    private LoginMember 로그인;
    private FavoriteRequest 즐겨찾기_신청;
    private Favorite 즐겨찾기_1;
    private Favorite 즐겨찾기_2;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        강남역 = initStation(강남역,"강남역", 1L);
        역삼역 = initStation(역삼역,"역삼역", 2L);
        삼성역 = initStation(삼성역,"삼성역", 3L);
        서초역 = initStation(서초역,"서초역", 4L);
        로그인 = new LoginMember(1L, EMAIL, PASSWORD, AGE);
        즐겨찾기_신청 = new FavoriteRequest(강남역.getId(), 역삼역.getId());
        즐겨찾기_1 = new Favorite(로그인.getId(), 강남역, 역삼역);
        ReflectionTestUtils.setField(즐겨찾기_1, "id", 1L);
        즐겨찾기_2 = new Favorite(로그인.getId(), 역삼역, 서초역);
        ReflectionTestUtils.setField(즐겨찾기_2, "id", 2L);
    }

    @Test
    void createFavorite() {
        // given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(역삼역.getId())).thenReturn(Optional.of(역삼역));
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기_1);

        // when
        FavoriteResponse 즐겨찾기_결과 = favoriteService.saveFavorites(로그인, 즐겨찾기_신청);

        // then
        assertThat(즐겨찾기_결과).isNotNull();
        assertThat(즐겨찾기_결과.getId()).isEqualTo(즐겨찾기_1.getId());
    }

    @Test
    void findFavorites() {
        // given
        when(favoriteRepository.findByMemberId(로그인.getId())).thenReturn(Lists.newArrayList(즐겨찾기_1, 즐겨찾기_2));

        // when
        List<FavoriteResponse> 즐겨찾기들 = favoriteService.findFavorites(로그인);

        // then
        assertThat(즐겨찾기들).hasSize(2);
        assertThat(즐겨찾기들.get(0).getId()).isEqualTo(즐겨찾기_1.getId());
    }

    @Test
    void deleteFavorites() {
        // when / then
        assertThatCode(() ->
            favoriteService.deleteFavoritesById(즐겨찾기_1.getId())
        ).doesNotThrowAnyException();
        verify(favoriteRepository).deleteById(anyLong());
    }

    private Station initStation(Station station, String stationName, Long id) {
        station = new Station(stationName);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}
