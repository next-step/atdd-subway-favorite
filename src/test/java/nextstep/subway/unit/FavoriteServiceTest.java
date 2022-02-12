package nextstep.subway.unit;

import nextstep.favorite.applicataion.FavoriteService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 출발역;
    private Station 도착역;
    private Member 회원;

    @BeforeEach
    void setUp() {
        출발역 = stationRepository.save(new Station("출발역"));
        도착역 = stationRepository.save(new Station("도착역"));
        회원 = memberRepository.save(new Member("e@mail.com", "password", 20));
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        //when
        Long favoriteId = favoriteService.createFavorite(회원.getId(), 출발역.getId(), 도착역.getId());

        //then
        assertThat(favoriteId).isPositive();

    }
}
