package nextstep.subway.unit;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    private Long memberId = 1L;
    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        강남역 = 역생성("강남역");
        양재역 = 역생성("양재역");
        판교역 = 역생성("판교역");
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 양재역.getId());

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(1L, request);

        // then
        assertThat(favoriteRepository.findById(favorite.getId()).isPresent()).isTrue();
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void showFavorites() {
        // given
        즐겨찾기생성(memberId, 강남역, 양재역);
        즐겨찾기생성(memberId, 강남역, 판교역);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteResponses(memberId);

        // then
        assertThat(favoriteResponses).hasSize(2);
        assertThat(favoriteResponses.get(0).getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponses.get(0).getTarget().getName()).isEqualTo("양재역");
        assertThat(favoriteResponses.get(1).getSource().getName()).isEqualTo("강남역");
        assertThat(favoriteResponses.get(1).getTarget().getName()).isEqualTo("판교역");
    }

    private Station 역생성(String name) {
        return stationRepository.save(new Station(name));
    }

    private FavoriteResponse 즐겨찾기생성(Long memberId, Station source, Station target) {
        return favoriteService.createFavorite(memberId, new FavoriteRequest(source.getId(), target.getId()));
    }
}
