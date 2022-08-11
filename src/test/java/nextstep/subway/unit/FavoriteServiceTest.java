package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 교대역;

    private Station 강남역;

    private Station 양재역;

    private Station 남부터미널역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setup() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        Line green = new Line("2호선", "green");
        green.addSection(교대역, 강남역, 10);
        이호선 = lineRepository.save(green);

        Line orange = new Line("3호선", "orange");
        orange.addSection(교대역, 남부터미널역, 2);
        orange.addSection(남부터미널역, 양재역, 3);
        삼호선 = lineRepository.save(orange);

        Line red = new Line("신분당선", "red");
        red.addSection(강남역, 양재역, 10);
        신분당선 = lineRepository.save(red);
    }

    public FavoriteRequest 즐겨찾기_등록_요청_param(Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        ReflectionTestUtils.setField(favoriteRequest, "source", source);
        ReflectionTestUtils.setField(favoriteRequest, "target", target);
        return favoriteRequest;
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void registerFavorite() {

        //given
        FavoriteRequest favoriteRequest = 즐겨찾기_등록_요청_param(교대역.getId(), 양재역.getId());

        //when
        Long 즐겨찾기식별자 = favoriteService.registerFavorite(favoriteRequest);

        //then
        assertThat(즐겨찾기식별자).isNotNull();
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {

        //given
        favoriteService.registerFavorite(즐겨찾기_등록_요청_param(교대역.getId(), 양재역.getId()));
        favoriteService.registerFavorite(즐겨찾기_등록_요청_param(남부터미널역.getId(), 양재역.getId()));

        //when
        List<FavoriteResponse> favorites = favoriteService.getFavorites();

        //then
        assertThat(favorites.size()).isEqualTo(2);
        assertThat(favorites.stream().map(FavoriteResponse::getSource).map(StationResponse::getName).collect(toList()))
                .containsExactly("교대역", "남부터미널역");

        assertThat(favorites.stream().map(FavoriteResponse::getTarget).map(StationResponse::getName).collect(toList()))
                .containsExactly("양재역", "양재역");
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {

        //given
        FavoriteRequest favoriteRequest = 즐겨찾기_등록_요청_param(교대역.getId(), 양재역.getId());

        Long 즐겨찾기식별자 = favoriteService.registerFavorite(favoriteRequest);

        //when
        boolean isDeleted = favoriteService.deleteFavorite(즐겨찾기식별자);

        //then
        assertThat(isDeleted).isTrue();
    }

    @DisplayName("즐겨찾기 삭제 시 해당 즐겨찾기가 없는경우")
    @Test
    void deleteFavoriteIfNotExisted() {

        //given
        final long 즐겨찾기식별자 = 1L;

        // when, then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(즐겨찾기식별자))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
