package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @Mock
    private StationService stationService;

    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    @BeforeEach
    void setup() {
        this.favoriteService = new FavoriteService(stationService, favoriteRepository);

        교대역 = new Station("교대역");
        ReflectionTestUtils.setField(교대역, "id", 1L);

        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 2L);

        양재역 = new Station("양재역");
        ReflectionTestUtils.setField(양재역, "id", 3L);

        남부터미널역 = new Station("남부터미널역");
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);
    }

    public FavoriteRequest createFavoriteRequestParams(Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        ReflectionTestUtils.setField(favoriteRequest, "source", source);
        ReflectionTestUtils.setField(favoriteRequest, "target", target);
        return favoriteRequest;
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void registerFavorite() {

        // given
        when(stationService.findById(1L)).thenReturn(교대역);
        when(stationService.findById(3L)).thenReturn(양재역);

        Favorite favorite = Favorite.register(교대역, 양재역);
        ReflectionTestUtils.setField(favorite, "id", 1L);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        // when
        Long 즐겨찾기식별자 = favoriteService.registerFavorite(createFavoriteRequestParams(교대역.getId(), 양재역.getId()));

        // then
        assertThat(즐겨찾기식별자).isNotNull();
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {

        // given
        Favorite 첫번째_즐겨찾기 = Favorite.register(교대역, 양재역);
        ReflectionTestUtils.setField(첫번째_즐겨찾기, "id", 1L);

        Favorite 두번째_즐겨찾기 = Favorite.register(남부터미널역, 양재역);
        ReflectionTestUtils.setField(두번째_즐겨찾기, "id", 2L);

        when(favoriteRepository.findAll()).thenReturn(Arrays.asList(첫번째_즐겨찾기, 두번째_즐겨찾기));

        // when
        List<FavoriteResponse> favorites = favoriteService.getFavorites();

        // then
        assertThat(favorites.size()).isEqualTo(2);

        assertThat(favorites.stream().map(FavoriteResponse::getSource).map(StationResponse::getName).collect(toList()))
                .containsExactly("교대역", "남부터미널역");

        assertThat(favorites.stream().map(FavoriteResponse::getTarget).map(StationResponse::getName).collect(toList()))
                .containsExactly("양재역", "양재역");
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {

        // given
        Favorite favorite = Favorite.register(교대역, 양재역);
        ReflectionTestUtils.setField(favorite, "id", 1L);

        when(favoriteRepository.findById(1L)).thenReturn(Optional.ofNullable(favorite));

        // when
        boolean isDeleted = favoriteService.deleteFavorite(favorite.getId());

        // then
        assertThat(isDeleted).isTrue();
    }
}
