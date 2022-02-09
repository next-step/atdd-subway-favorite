package nextstep.subway.applicaion.query;

import nextstep.exception.FavoriteNotFoundException;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.repository.FavoriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 조회 서비스")
@SpringBootTest
@Transactional
class FavoriteQueryServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    private FavoriteQueryService service;
    private Station 강남역;
    private Station 판교역;
    private long memberId = 1L;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");

        stationRepository.save(강남역);
        stationRepository.save(판교역);
        favoriteRepository.save(Favorite.of(memberId, 강남역, 판교역));

        service = new FavoriteQueryService(favoriteRepository);
    }

    @DisplayName("즐겨찾기 모두 조회")
    @Test
    void findFavorites() {
        // when
        List<FavoriteResponse> favorite = service.findFavorites(memberId);

        // then
        assertThat(favorite.get(0).getSource().getName()).isEqualTo(강남역.getName());
        assertThat(favorite.get(0).getTarget().getName()).isEqualTo(판교역.getName());
    }

    @DisplayName("즐겨찾기 id가 없는 경우 예외 처리")
    @Test
    void findFavorite_notFound() {
        // given
        long 즐겨찾기ID = 100L;

        // then
        Assertions.assertThatThrownBy(() -> service.findFavorite(즐겨찾기ID))
                .isInstanceOf(FavoriteNotFoundException.class);
    }

}