package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 역삼역;
    private Member 사용자;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        사용자 = memberRepository.save(new Member("email@email.com", "password", 30));
    }

    @Test
    void deleteFavorite() {
        // given
        Favorite 즐겨찾기 = favoriteRepository.save(new Favorite(강남역, 역삼역, 사용자));

        // when
        favoriteService.deleteFavorite(즐겨찾기.getId(), 사용자.getId());

        // then
        assertFalse(favoriteRepository.findById(즐겨찾기.getId()).isPresent());
    }
}
