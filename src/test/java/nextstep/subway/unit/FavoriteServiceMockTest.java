package nextstep.subway.unit;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    private Station 교대역;
    private Station 강남역;
    private Station 남부터미널역;
    private Station 양재역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private StationService stationService;

    @Mock
    private PathService pathService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private LineService lineService;

    /**
     * 교대역   --- *2호선*(5) ---   강남역
     * |                            |
     * *3호선(3)*                 *신분당선*(5)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재역
     */
    @BeforeEach
    public void setUp() {
        pathService = new PathService(lineService, stationService);
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        신분당선 = new Line("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 5);
        이호선 = new Line("이호선", "green");
        이호선.addSection(교대역, 강남역, 5);
        삼호선 = new Line("삼호선", "orange");
        삼호선.addSection(교대역, 양재역, 5);
        삼호선.addSection(남부터미널역, 양재역, 2);
    }

    @Test
    void 즐겨찾기_구간을_추가한다() {
        // given
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(4L)).willReturn(양재역);
        lenient().when(lineService.findLines()).thenReturn(List.of(이호선, 삼호선, 신분당선));
        given(favoriteRepository.save(any())).willReturn(new Favorite(1L, 교대역, 양재역));

        // when
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(1L, 1L, 4L);

        // then
        assertAll(() -> {
            assertThat(favoriteResponse.getSource().getName()).isEqualTo("교대역");
            assertThat(favoriteResponse.getTarget().getName()).isEqualTo("양재역");
        });
    }

    @Test
    void 즐겨찾기_구간을_추가시_서로_같은_구간은_추가시_예외를_발생시킨다() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                favoriteService.addFavorite(1L, 1L, 1L));
    }
}