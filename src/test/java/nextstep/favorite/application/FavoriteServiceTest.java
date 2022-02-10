package nextstep.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private StationRepository stationRepository;

    private Station 역1;
    private Station 역2;

    @BeforeEach
    public void setUp() {
        // given 역이 등록되어 있다.
        역1 = stationRepository.save(new Station("역1"));
        역2 = stationRepository.save(new Station("역2"));
    }

    @Test
    void saveFavoriteTest() {
        FavoriteResponse response = favoriteService.saveFavorite(FavoriteRequest.of(역1.getId(), 역2.getId()));

        assertThat(response.getSource().getId()).isEqualTo(역1.getId());
        assertThat(response.getTarget().getId()).isEqualTo(역2.getId());
    }

    @Test
    void findAllFavoriteTest() {
        // given
        favoriteService.saveFavorite(FavoriteRequest.of(역1.getId(), 역2.getId()));

        // when
        List<FavoriteResponse> responseList = favoriteService.findAllFavorite();

        // then
        assertThat(responseList.size()).isEqualTo(1);
        assertThat(responseList.stream()
            .findFirst().get().getSource().getName()).isEqualTo("역1");
        assertThat(responseList.stream()
            .findFirst().get().getTarget().getName()).isEqualTo("역2");
    }

    @Test
    void deleteByIdTest() {
        // given
        Long id = favoriteService.saveFavorite(FavoriteRequest.of(역1.getId(), 역2.getId())).getId();

        // when
        favoriteService.deleteById(id);

        // then
        List<FavoriteResponse> responseList = favoriteService.findAllFavorite();
        assertThat(responseList.isEmpty()).isTrue();
    }

}