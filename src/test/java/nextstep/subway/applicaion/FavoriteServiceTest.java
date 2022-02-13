package nextstep.subway.applicaion;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Member member;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("email@email.com", "password", 20));
        source = stationRepository.save(new Station("강남역"));
        target = stationRepository.save(new Station("남부터미널역"));
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        final Long favoriteId = favoriteService.createFavorite(member.getId(), source.getId(), target.getId());

        assertThat(favoriteId).isEqualTo(1L);
    }
}
