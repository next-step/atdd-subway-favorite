package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    private Long 로그인한_회원_아이디 = 1L;
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 시청역;
    private Station 서울역;

    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        시청역 = stationRepository.save(new Station("시청역"));
        서울역 = stationRepository.save(new Station("서울역"));

        일호선 = new Line("일호선", "blue");
        이호선 = new Line("이호선", "green");
        삼호선 = new Line("삼호선", "orange");
        신분당선 = new Line("신분당선", "red");

        일호선.addSection(시청역, 서울역, 3);
        이호선.addSection(교대역, 강남역, 5);
        삼호선.addSection(교대역, 남부터미널역, 5);
        신분당선.addSection(강남역, 양재역, 10);

        lineRepository.save(일호선);
        lineRepository.save(이호선);
        lineRepository.save(삼호선);
        lineRepository.save(신분당선);
    }

    @DisplayName("즐겨찾기 추가 하기")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 남부터미널역.getId());

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest, 로그인한_회원_아이디);

        // then
        assertAll(
            () -> assertThat(favoriteResponse.getMemberId()).isEqualTo(로그인한_회원_아이디),
            () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName()),
            () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(남부터미널역.getName())
        );
    }

    @DisplayName("즐겨찾기 추가 하기 - source로 부터 target 까지 경로가 없는 경우")
    @Test
    void createFavoriteDoesNotExistPath() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 시청역.getId());

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.createFavorite(favoriteRequest, 로그인한_회원_아이디));
    }

    @DisplayName("즐겨찾기 조회 하기")
    @Test
    void getFavorites() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 남부터미널역.getId());
        favoriteService.createFavorite(favoriteRequest, 로그인한_회원_아이디);

        // when
        List<FavoriteResponse> favoritesResponse = favoriteService.findFavorites(로그인한_회원_아이디);

        // then
        assertThat(favoritesResponse.size()).isEqualTo(1);
    }

    @DisplayName("즐겨찾기 삭제 하기")
    @Test
    void deleteFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 남부터미널역.getId());
        FavoriteResponse createdFavorite = favoriteService.createFavorite(favoriteRequest, 로그인한_회원_아이디);

        // when
        favoriteService.deleteFavorite(createdFavorite.getId(), 로그인한_회원_아이디);

        // then
        assertThat(favoriteService.findFavorites(로그인한_회원_아이디).size()).isEqualTo(0);
    }

}
