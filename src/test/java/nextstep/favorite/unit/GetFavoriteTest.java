package nextstep.favorite.unit;

import nextstep.favorite.application.GetFavoriteService;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.LoginMemberForFavorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.SectionRepository;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 조회 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetFavoriteTest {

    @Autowired
    private GetFavoriteService getFavoriteService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Member 최현구;

    @BeforeEach
    void setUp() {
        favoriteRepository.deleteAllInBatch();
        sectionRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));

        lineRepository.save(new Line("2호선", "green", 강남역, 역삼역, 10L));

        최현구 = memberRepository.save(new Member("jinha3507@gmail.com", "password", 29));
    }

    @DisplayName("내 계정의 즐겨찾기 목록을 조회할 수 있다.")
    @Nested
    class GetFavorites {
        @DisplayName("즐겨찾기가 없는 경우 빈 목록을 조회할 수 있다.")
        @Test
        void getFavoritesWithoutFavorites() {
            // given
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite(최현구.getEmail());

            // when
            List<FavoriteResponse> favoriteResponses = getFavoriteService.getFavorites(loginMember);

            // then
            assertThat(favoriteResponses).isEmpty();
        }

        @DisplayName("즐겨찾기가 있는 경우 즐겨찾기 목록을 조회할 수 있다.")
        @Test
        void getFavoritesWithFavorites() {
            // given
            favoriteRepository.save(new Favorite(최현구.getId(), 강남역.getId(), 역삼역.getId()));
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite(최현구.getEmail());

            // when
            List<FavoriteResponse> favoriteResponses = getFavoriteService.getFavorites(loginMember);

            // then
            assertThat(favoriteResponses).hasSize(1);
            assertThat(favoriteResponses.get(0).getSource().getName()).isEqualTo(강남역.getName());
            assertThat(favoriteResponses.get(0).getTarget().getName()).isEqualTo(역삼역.getName());
        }

        @DisplayName("멤버 정보가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void getFavoritesWithNotExistsMember() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("abcde@naver.com");

            assertThatThrownBy(() -> getFavoriteService.getFavorites(loginMember))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + loginMember.getEmail());
        }
    }
}
