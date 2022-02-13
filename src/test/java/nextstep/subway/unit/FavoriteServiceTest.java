package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Member member;
    private Member other;
    private Station 강남역;
    private Station 남부터미널역;
    private Station 교대역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("email@email.com", "password", 20));
        other = memberRepository.save(new Member("email@email.com", "password", 20));

        강남역 = stationRepository.save(new Station("강남역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        교대역 = stationRepository.save(new Station("교대역"));
        양재역 = stationRepository.save(new Station("영재역"));
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        final Long favoriteId = favoriteService.createFavorite(member.getId(), 강남역.getId(), 남부터미널역.getId());

        assertThat(favoriteId).isNotNull();
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAllFavorite() {
        favoriteService.createFavorite(member.getId(), 강남역.getId(), 남부터미널역.getId());
        favoriteService.createFavorite(member.getId(), 교대역.getId(), 양재역.getId());

        List<Favorite> favorites = favoriteService.findAllFavorite(member.getId());

        assertThat(favorites.size()).isEqualTo(2);
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        final Long favoriteId = favoriteService.createFavorite(member.getId(), 강남역.getId(), 남부터미널역.getId());

        favoriteService.delete(member.getId(), favoriteId);
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제할  수 없다")
    @Test
    void deleteFavoriteByOther() {
        final Long favoriteId = favoriteService.createFavorite(member.getId(), 강남역.getId(), 남부터미널역.getId());

        assertThatThrownBy(() -> favoriteService.delete(other.getId(), favoriteId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("it is not member's favorite");
    }
}
