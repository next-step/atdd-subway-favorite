package nextstep.subway.unit;

import nextstep.common.NotFoundStationException;
import nextstep.marker.ClassicUnitTest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.controller.request.FavoriteCreateRequest;
import nextstep.subway.controller.resonse.FavoriteResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.FavoriteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ClassicUnitTest
class FavoriteServiceTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;


    private Line 이호선;
    private Line 신설동선;
    private Station 강남역;
    private Station 언주역;
    private Station 성수역;
    private Station 신설동역;
    private Member 회원;

    @BeforeEach
    void setUp() {
        강남역 = getStation("강남역");
        언주역 = getStation("언주역");
        성수역 = getStation("성수역");
        신설동역 = getStation("신설동역");
        이호선 = getLine("2호선", "bg-green-300", 강남역, 언주역, 10L);
        신설동선 = getLine("신설동선", "bg-red-300", 성수역, 신설동역, 7L);
        회원 = getMember("email", "password", 20);
    }

    @Test
    void 경로가_연결된_노선인_경우_즐겨찾기_등록_성공() {
        // given
        FavoriteCreateRequest request = new FavoriteCreateRequest(강남역.getId(), 언주역.getId());

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(회원.getEmail(), request);

        // then
        verifyFavoriteResponse(favoriteResponse, 강남역.getName(), 언주역.getName());
    }

    @Test
    void 경로가_연결된_노선이_아닌_경우_즐겨찾기_등록_실패() {
        // given
        FavoriteCreateRequest request = new FavoriteCreateRequest(강남역.getId(), 성수역.getId());

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> favoriteService.createFavorite(회원.getEmail(), request));
    }

    @Test
    void 존재하지_않는_역인_경우_즐겨찾기_등록_실패() {
        // given
        long unRegisteredStationId = 100L;
        FavoriteCreateRequest request = new FavoriteCreateRequest(unRegisteredStationId, 성수역.getId());

        // when & then
        Assertions.assertThrows(NotFoundStationException.class, () -> favoriteService.createFavorite(회원.getEmail(), request));
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
