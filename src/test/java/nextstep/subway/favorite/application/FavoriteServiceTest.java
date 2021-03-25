package nextstep.subway.favorite.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("지하철 즐겨찾기 비즈니스 로직 단위 테스트")
@SpringBootTest
@Transactional
class FavoriteServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private FavoriteService favoriteService;

    private Station savedStationGangnam;
    private Station savedStationCheonggyesan;

    private LineRequest newBundangRequest;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        // given
        savedStationGangnam = stationRepository.save(new Station("강남역"));
        savedStationCheonggyesan = stationRepository.save(new Station("청계산입구역"));

        newBundangRequest = new LineRequest("신분당선", "bg-red-600", savedStationGangnam.getId(), savedStationCheonggyesan.getId(), 10);
        lineService.saveLine(newBundangRequest);

        savedMember = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
    }

    @Test
    @DisplayName("즐겨찾기 추가") // TODO : 로그인 여부 확인할 Interceptor 구현
    void createFavorite() {
        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(savedStationGangnam, savedStationCheonggyesan);
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest);

        // then
        assertThat(favoriteResponse.getFavorites).hasSize(1);
        assertThat(favoriteResponse.getFavorites).containsExactlyElementsOf(savedStationGangnam, savedStationCheonggyesan);
    }
}
