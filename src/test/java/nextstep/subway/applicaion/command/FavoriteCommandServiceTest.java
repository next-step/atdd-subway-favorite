package nextstep.subway.applicaion.command;

import nextstep.exception.StationNotFoundException;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 command 테스트")
@Transactional
@SpringBootTest
class FavoriteCommandServiceTest {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private StationRepository stationRepository;

    private FavoriteCommandService service;
    private Station 강남역;
    private Station 판교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");

        stationRepository.save(강남역);
        stationRepository.save(판교역);

        service = new FavoriteCommandService(favoriteRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        long id = 1L;
        FavoriteRequest 요청 = FavoriteRequest.of(강남역.getId(), 판교역.getId());

        // when
        service.createFavorite(id, 요청);

        // then
        List<Favorite> favorites = favoriteRepository.findAll();
        assertThat(favorites.size()).isEqualTo(1);
    }

    @DisplayName("없는 역을 즐겨찾기 생성 하면 실패")
    @Test
    void createFavorite_fail() {
        // given
        long id = 1L;
        FavoriteRequest 요청 = FavoriteRequest.of(강남역.getId(), 1000L);

        // then
        assertThatThrownBy(() -> service.createFavorite(id, 요청))
                .isInstanceOf(StationNotFoundException.class);
    }

}