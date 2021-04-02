package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DisplayName("즐겨찾기 관련 서비스 테스트")
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private StationRepository stationRepository;

    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        stationRepository.save(강남역);
        stationRepository.save(역삼역);
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void create() {
        // given
        Long source = 1L;
        Long target = 2L;

        // when
        FavoriteResponse response = favoriteService.create(source, target);

        // then
        assertThat(response.getId()).isEqualTo(1L);
    }
}
