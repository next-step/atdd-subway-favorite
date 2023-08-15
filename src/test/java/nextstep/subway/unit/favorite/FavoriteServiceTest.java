package nextstep.subway.unit.favorite;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.subway.application.FavoriteService;
import nextstep.subway.application.dto.favorite.FavoriteRequest;
import nextstep.subway.application.dto.favorite.FavoriteResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
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
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberService memberService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final int AGE = 20;

    @BeforeEach
    void setUpFavoriteServiceTest() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        FavoriteRequest request = new FavoriteRequest(교대역.getId(), 양재역.getId());
        FavoriteResponse response = favoriteService.createFavorite(EMAIL, request);

        // then
        Assertions.assertThat(response.getId()).isEqualTo(1L);
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(교대역.getId(), 양재역.getId());
        FavoriteResponse response = favoriteService.createFavorite(EMAIL, request);

        // when


        // then
    }
}