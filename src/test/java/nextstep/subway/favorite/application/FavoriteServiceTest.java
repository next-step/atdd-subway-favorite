package nextstep.subway.favorite.application;

import nextstep.subway.auth.exception.AuthenticationFailException;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    private static long MEMBER_ID = 1L;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 신림역;
    private Station 봉천역;
    private Station 사당역;
    private Station 서초역;
    private FavoriteRequest 즐겨찾기_등록;

    @BeforeEach
    void setUp() {
        신림역 = stationRepository.save(new Station("신림역"));
        봉천역 = stationRepository.save(new Station("봉천역"));
        사당역 = stationRepository.save(new Station("사당역"));
        서초역 = stationRepository.save(new Station("서초역"));

        즐겨찾기_등록 = new FavoriteRequest(신림역.getId(), 봉천역.getId());
    }


    @Test
    void 즐겨찾기_등록() {
        Long savedFavoriteId = favoriteService.save(MEMBER_ID, 즐겨찾기_등록).getId();

        assertThat(savedFavoriteId).isNotNull();
    }

    @Test
    void 즐겨찾기_목록_조회() {
        favoriteService.save(MEMBER_ID, 즐겨찾기_등록);

        FavoriteRequest favoriteRequest2 = new FavoriteRequest(신림역.getId(), 사당역.getId());
        favoriteService.save(MEMBER_ID, favoriteRequest2);

        List<FavoriteResponse> favoriteResponses = favoriteService.getAll(MEMBER_ID);

        assertThat(favoriteResponses).hasSize(2);

    }

    @Test
    void 즐겨찾기_삭제() {
        FavoriteResponse favoriteResponse = favoriteService.save(MEMBER_ID, 즐겨찾기_등록);

        FavoriteRequest 즐겨찾기_등록_2 = new FavoriteRequest(봉천역.getId(), 서초역.getId());
        favoriteService.save(MEMBER_ID, 즐겨찾기_등록_2);

        favoriteService.delete(favoriteResponse.getId(), MEMBER_ID);

        List<FavoriteResponse> favoriteResponses = favoriteService.getAll(MEMBER_ID);
        assertThat(favoriteResponses).hasSize(1);
    }


    @Test
    void 다른_유저_즐겨찾기_제거_예외() {
        FavoriteResponse response = favoriteService.save(MEMBER_ID, 즐겨찾기_등록);

        assertThatThrownBy(() -> favoriteService.delete(response.getId(), 2L))
                .isInstanceOf(AuthenticationFailException.class);
    }

    @Test
    void 미등록된_지하철_즐겨찾기_등록시_예외() {
        FavoriteRequest 즐겨찾기_등록_3 = new FavoriteRequest(6L, 신림역.getId());

        assertThatThrownBy(() -> favoriteService.save(MEMBER_ID, 즐겨찾기_등록_3))
                .isInstanceOf(RuntimeException.class);
    }
}
