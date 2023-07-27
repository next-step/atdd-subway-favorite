package nextstep.subway.unit;

import nextstep.marker.ClassicUnitTest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.PathFindService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ClassicUnitTest
class FavoriteServiceTest {

    @Autowired
    private PathFindService pathFindService;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;


    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 언주역;
    private Station 성수역;
    private Member 회원;

    @BeforeEach
    void setUp() {
        강남역 = getStation("강남역");
        언주역 = getStation("언주역");
        성수역 = getStation("성수역");
        이호선 = getLine("2호선", "bg-green-300", 강남역, 언주역, 10L);
        삼호선 = getLine("3호선", "bg-red-300", 언주역, 성수역, 7L);
        회원 = getMember("email", "password", 20);
    }

    @Test
    void 경로가_연결된_노선인_경우_즐겨찾기_등록_성공() {
        // given
        FavoriteCreateRequest request = new FavoriteCreateRequest(강남역.getId(), 언주역.getId());

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(request);

        // then
        verifyFavoriteResponse(favoriteResponse, 강남역.getName(), 언주역.getName());
    }

    private Line getLine(String name, String color, Station upStation, Station downStation, long distance) {
        Line secondaryLine = Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
        return lineRepository.save(secondaryLine);
    }

    private Station getStation(String name) {
        return stationRepository.save(Station.create(() -> name));
    }

    private Member getMember(String email, String password, int age) {
        return memberRepository.save(new Member(email, password, age));

    }

    private void verifyFavoriteResponse(FavoriteResponse favoriteResponse, String sourceStationName, String targetStationName) {
        Assertions.assertEquals(sourceStationName, favoriteResponse.getSource().getName());
        Assertions.assertEquals(targetStationName, favoriteResponse.getTarget().getName());
    }
}
