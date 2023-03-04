package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteServiceTest extends SpringTest {

    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    void setUp() {
        member = createMember("email@email.com", "password", 20);
        sourceStation = createStation("강남역");
        targetStation = createStation("역삼역");
    }

    @DisplayName("즐겨찾기 저장")
    @Test
    void saveFavorite() {
        // given
        final FavoriteRequest favoriteRequest = createFavoriteRequest(sourceStation.getId(), targetStation.getId());

        // when
        favoriteService.saveFavorite(member.getEmail(), favoriteRequest);

        // then
        final List<Favorite> favorites = favoriteRepository.findAll();
        assertThat(favorites.stream()
                .filter(favorite -> favorite.getMemberId().equals(member.getId()))
                .filter(favorite -> favorite.getSourceStationId().equals(sourceStation.getId()))
                .filter(favorite -> favorite.getTargetStationId().equals(targetStation.getId()))
                .findFirst()).isPresent();
    }

    @DisplayName("이미 존재하는 즐겨찾기 항목 재저장 시, 오류 발생")
    @Test
    void saveFavoriteWhenIsAlreadyExists() {
        // given
        final FavoriteRequest favoriteRequest = createFavoriteRequest(sourceStation.getId(), targetStation.getId());
        favoriteService.saveFavorite(member.getEmail(), favoriteRequest);

        // when, then
        assertThatThrownBy(() -> {
            favoriteService.saveFavorite(member.getEmail(), favoriteRequest);
        }).isInstanceOf(RuntimeException.class);
    }

    private Member createMember(final String email, final String password, final int age) {
        return memberRepository.save(new Member(email, password, age));
    }

    private Station createStation(final String name) {
        return stationRepository.save(new Station(name));
    }

    private FavoriteRequest createFavoriteRequest(final long sourceStationId, final long targetStationId) {
        return new FavoriteRequest(
                String.valueOf(sourceStationId),
                String.valueOf(targetStationId));
    }
}
