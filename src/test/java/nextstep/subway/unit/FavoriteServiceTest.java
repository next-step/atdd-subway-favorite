package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.NotOwnerFavoriteException;
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

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    Station 강남역;
    Station 역삼역;
    String 관리자_이메일 = "admin@email.com";
    String 회원_이메일 = "member@email.com";

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
    }

    @Test
    @DisplayName("즐겨찾기 추가하는 테스트입니다.")
    void saveFavoriteTest() {
        Favorite favorite = favoriteService.saveFavorite(관리자_이메일, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        Favorite 비교값 = favoriteRepository.findById(favorite.getId()).get();

        assertThat(favorite).isEqualTo(비교값);
    }

    @Test
    @DisplayName("즐겨찾기 목록을 조회합니다.")
    void findFavoriteTest() {
        favoriteService.saveFavorite(관리자_이메일, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        FavoriteResponse favoriteResponse = favoriteService.findFavorites(관리자_이메일).get(0);

        assertThat(favoriteResponse.getSource()).isEqualTo(FavoriteStationResponse.of(강남역));
        assertThat(favoriteResponse.getTarget()).isEqualTo(FavoriteStationResponse.of(역삼역));
    }

    @Test
    @DisplayName("즐겨찾기 삭제합니다.")
    void deleteFavoriteTest() {
        Favorite favorite = favoriteService.saveFavorite(관리자_이메일, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        favoriteService.deleteFavorite(관리자_이메일, favorite.getId());

        assertThat(favoriteService.findFavorites(관리자_이메일)).hasSize(0);
    }

    @Test
    @DisplayName("즐겨찾기 삭제 시, 자신것이 아닐때 에러를 반환합니다.")
    void deleteFavoriteException() {
        Favorite favorite = favoriteService.saveFavorite(관리자_이메일, new FavoriteRequest(강남역.getId(), 역삼역.getId()));

        assertThatExceptionOfType(NotOwnerFavoriteException.class).isThrownBy(() -> {
            favoriteService.deleteFavorite(회원_이메일, favorite.getId());
        }).withMessage("자신의 즐겨찾기만 제거가 가능합니다.");

    }

}
