package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(savedStationGangnam.getId(), savedStationCheonggyesan.getId());
        Long savedFavoriteId = favoriteService.add(savedMember.getId(), favoriteRequest);

        // then
        FavoriteResponse response = favoriteService.findFavoriteResponseById(savedFavoriteId);

        assertThat(response.getId()).isEqualTo(savedFavoriteId);
        assertThat(savedMember.getFavorites()).hasSize(1);
    }
}
