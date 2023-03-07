package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

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
    private Member etcMember;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    void setUp() {
        member = createMember("email@email.com", "password", 20);
        etcMember = createMember("etc@email.com", "password", 20);
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

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findFavorites() {
        // given
        final Favorite favorite1 = createFavorite(member.getId(), sourceStation.getId(), targetStation.getId());
        final Favorite favorite2 = createFavorite(member.getId(), sourceStation.getId(), targetStation.getId());

        // when
        final List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(member.getEmail());

        // then
        final List<Long> favoriteIds = favoriteResponses.stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());
        assertThat(favoriteIds).containsOnly(favorite1.getId(), favorite2.getId());
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        final Favorite favorite = createFavorite(member.getId(), sourceStation.getId(), targetStation.getId());

        // when
        favoriteService.deleteFavorite(member.getEmail(), favorite.getId());

        // then
        final List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(member.getEmail());
        final List<Long> favoriteIds = favoriteResponses.stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());
        assertThat(favoriteIds).doesNotContain(favorite.getId());
    }

    @DisplayName("존재하지 않는 즐겨찾기 삭제 시, 에러 발생")
    @Test
    void deleteFavoriteWhatIsNotExists() {
        // when, then
        assertThatThrownBy(() -> {
            favoriteService.deleteFavorite(member.getEmail(), 1L);
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("자신이 등록하지 않은 즐겨찾기 삭제 시, 에러 발생")
    @Test
    void deleteFavoriteWithNotOwn() {
        // given
        final Favorite favorite = createFavorite(etcMember.getId(), sourceStation.getId(), targetStation.getId());

        // when, then
        assertThatThrownBy(() -> {
            favoriteService.deleteFavorite(member.getEmail(), favorite.getId());
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("해당 즐겨찾기의 등록자가 아닙니다.");
    }

    private Member createMember(final String email, final String password, final int age) {
        return memberRepository.save(new Member(email, password, age));
    }

    private Station createStation(final String name) {
        return stationRepository.save(new Station(name));
    }

    private Favorite createFavorite(final long memberId, final long sourceStationId, final long targetStationId) {
        return favoriteRepository.save(new Favorite(memberId, sourceStationId, targetStationId));
    }

    private FavoriteRequest createFavoriteRequest(final long sourceStationId, final long targetStationId) {
        return new FavoriteRequest(
                String.valueOf(sourceStationId),
                String.valueOf(targetStationId));
    }
}
