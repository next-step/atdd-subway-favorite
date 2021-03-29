package nextstep.subway.favorite.application;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.favorite.dto.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@DisplayName("지하철 즐겨찾기 기능 Service")
public class FavoriteServiceTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Line 신분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Station 광교역;

    private FavoriteService favoriteService;

    private Long memberId;

    @BeforeEach
    public void setUp() {

        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        정자역 = stationRepository.save(new Station("정자역"));
        광교역 = stationRepository.save(new Station("광교역"));

        신분당선 = lineRepository.save(new Line("신분당선", "red", 강남역, 양재역, 10));

        favoriteService = new FavoriteService(stationService, favoriteRepository);

        memberId = 1L;
    }


    @DisplayName("즐겨찾기 조회")
    @Test
    public void inquiryFavorite() {
        //given
        favoriteService.createFavorite(FavoriteRequest.of(강남역.getId(), 양재역.getId()), memberId);

        //when
        List<FavoriteResponse> favorites = favoriteService.findByMemberId(memberId);

        //then
        assertThat(favorites.size()).isEqualTo(1);
    }


    @DisplayName("즐겨찾기 등록")
    @Test
    public void createFavorite() {
        //when
        FavoriteResponse response = favoriteService.createFavorite(FavoriteRequest.of(강남역.getId(), 양재역.getId()), memberId);

        //then
        assertThat(response.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(response.getTarget().getId()).isEqualTo(양재역.getId());
    }

    @DisplayName("같은 source/target으로 즐겨찾기 등록시 실패")
    @Test
    public void createFavoriteWithSameSourceTarget() {
        assertThatThrownBy(()-> favoriteService.createFavorite(FavoriteRequest.of(강남역.getId(), 강남역.getId()), memberId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("시작역과 도착역은 동일할 수 없습니다.");
    }
}
