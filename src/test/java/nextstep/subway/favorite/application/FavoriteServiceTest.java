package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FavoriteServiceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private MemberResponse memberResponse;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private StationService stationService;

    @BeforeEach
    void setUp() {
        교대역 = stationService.saveStation(new StationRequest("교대역"));
        강남역 = stationService.saveStation(new StationRequest("강남역"));
        memberResponse = memberService.createMember(new MemberRequest("email@email.com", "password", 20));
    }

    @Test
    @DisplayName("즐겨찾기를 저장한다")
    void createFavorite() {
        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(memberResponse.getId(), new FavoriteRequest(교대역.getId(), 강남역.getId()));

        // then
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(교대역.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(강남역.getId());
    }
}