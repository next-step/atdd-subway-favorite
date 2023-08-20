package nextstep.subway.unit.favorite;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.subway.application.FavoriteService;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.favorite.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MemberService memberService;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final int AGE = 20;

    @BeforeEach
    void setUpFavoriteServiceTest() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));
        교대역 = stationRepository.save(new Station("교대역")).getId();
        강남역 = stationRepository.save(new Station("강남역")).getId();
        양재역 = stationRepository.save(new Station("양재역")).getId();
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        FavoriteRequest request = new FavoriteRequest(교대역, 양재역);
        FavoriteResponse response = favoriteService.createFavorite(EMAIL, request);

        // then
        Assertions.assertThat(response.getSource().getId()).isEqualTo(교대역);
        Assertions.assertThat(response.getTarget().getId()).isEqualTo(양재역);
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(교대역, 양재역);
        favoriteService.createFavorite(EMAIL, request);

        // when
        List<FavoriteResponse> responseList = favoriteService.findFavorite(EMAIL);

        // then
        FavoriteResponse response = responseList.get(0);
        Assertions.assertThat(response.getSource().getId()).isEqualTo(교대역);
        Assertions.assertThat(response.getTarget().getId()).isEqualTo(양재역);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(교대역, 양재역);
        FavoriteResponse response = favoriteService.createFavorite(EMAIL, request);

        // when
        favoriteService.deleteFavorite(response.getId());

        // then
        Assertions.assertThat(favoriteService.findFavorite(EMAIL)).isEmpty();
    }

    @DisplayName("조회되지 않는 역에 대해서 즐겨찾기를 등록할 경우 예외가 발생한다.")
    @Test
    void notSavedStationException() {
        FavoriteRequest request = new FavoriteRequest(교대역, 10L);
        Assertions.assertThatThrownBy(() -> favoriteService.createFavorite(EMAIL, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Check station id");
    }
}