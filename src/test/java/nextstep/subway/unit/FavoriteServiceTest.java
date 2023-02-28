package nextstep.subway.unit;

import nextstep.auth.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.FavoriteIsNotYoursException;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    private Member memberA;
    private Member memberB;
    private Station stationA;
    private Station stationB;

    private FavoriteRequest favoriteRequest;

    private LoginMember loginMemberA;
    private LoginMember loginMemberB;

    @BeforeEach
    void setUp() {
        memberA = memberRepository.save(new Member("memberA"));
        memberB = memberRepository.save(new Member("memberB"));
        stationA = stationRepository.save(new Station("stationA"));
        stationB = stationRepository.save(new Station("stationB"));

        favoriteRequest = new FavoriteRequest(stationA.getId(), stationB.getId());

        loginMemberA = new LoginMember(memberA.getId());
        loginMemberB = new LoginMember(memberB.getId());
    }

    @DisplayName("로그인 후 즐겨찾기를 추가할 수 있다.")
    @Test
    void addFavorite() {
        final var favoriteResponse = favoriteService.addFavorite(loginMemberA, favoriteRequest);

        assertAll(
                () -> assertThat(favoriteResponse.getId()).isNotNull(),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(stationA.getId()),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(stationB.getId())
        );
    }

    @DisplayName("경로 즐겨찾기를 추가하고 즐겨찾기 목록을 조회하면 추가한 즐겨찾기를 찾을 수 있다.")
    @Test
    void findFavorites() {
        final var favoriteResponse = favoriteService.addFavorite(loginMemberA, favoriteRequest);

        final var favorites = favoriteService.findFavorites(loginMemberA);

        assertThat(favorites).containsExactly(favoriteResponse);
    }

    @DisplayName("즐겨찾기로 추가한 경로를 삭제할 수 있다.")
    @Test
    void removeFavorite() {
        final var favoriteResponse = favoriteService.addFavorite(loginMemberA, favoriteRequest);

        favoriteService.removeFavorite(loginMemberA, favoriteResponse.getId());

        final var favorites = favoriteService.findFavorites(loginMemberA);
        assertThat(favorites).isEmpty();
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제하면 오류가 발생한다.")
    @Test
    void removeOtherMemberFavorite() {
        final var favoriteResponse = favoriteService.addFavorite(loginMemberA, favoriteRequest);

        assertThatThrownBy(() -> favoriteService.removeFavorite(loginMemberB, favoriteResponse.getId()))
                .isInstanceOf(FavoriteIsNotYoursException.class);
    }
}
