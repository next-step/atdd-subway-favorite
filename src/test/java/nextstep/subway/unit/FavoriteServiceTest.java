package nextstep.subway.unit;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    StationRepository stationRepository;

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 양재역 = stationRepository.save(new Station("양재역"));
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 양재역.getId());

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(1L, request);

        // then
        assertThat(favoriteRepository.findById(favorite.getId()).isPresent()).isTrue();
    }

}
