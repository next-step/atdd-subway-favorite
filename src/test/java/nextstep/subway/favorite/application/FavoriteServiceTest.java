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

import java.util.List;

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
        FavoriteResponse response = 즐겨찾기_생성_요청(source, target, 1L);

        // then
        assertThat(response.getId()).isEqualTo(1L);
    }

    @DisplayName("즐겨찾기 목록을 조회한다")
    @Test
    void findAllByMemberId() {
        // when
        즐겨찾기_생성_요청됨(1L, 2L, 1L);
        List<FavoriteResponse> response = favoriteService.findAllByMemberId(1L);

        // then
        assertThat(response.get(0).getSource()).isEqualTo(강남역);
        assertThat(response.get(0).getTarget()).isEqualTo(역삼역);
    }

    private FavoriteResponse 즐겨찾기_생성_요청됨(Long source, Long target, Long memberId) {
        return 즐겨찾기_생성_요청(source, target, memberId);
    }

    private FavoriteResponse 즐겨찾기_생성_요청(Long source, Long target, Long memberId) {
        return favoriteService.create(source, target, memberId);
    }
}
