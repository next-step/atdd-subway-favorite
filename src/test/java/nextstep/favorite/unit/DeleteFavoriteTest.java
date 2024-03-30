package nextstep.favorite.unit;

import nextstep.favorite.application.DeleteFavoriteService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 삭제 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteFavoriteTest {

    @Autowired
    private DeleteFavoriteService deleteFavoriteService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

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
        선릉역 = stationRepository.save(new Station("선릉역"));

        lineRepository.save(new Line("2호선", "green", 강남역, 역삼역, 10L));

        최현구 = memberRepository.save(new Member("jinha3507@gmail.com", "password", 29));
    }

    @DisplayName("내 계정의 즐겨찾기 삭제")
    @Nested
    class DeleteFavorite {
        @DisplayName("즐겨찾기를 삭제한다.")
        @Test
        void deleteFavorite() {
            // given
            Favorite favorite = favoriteRepository.save(new Favorite(최현구.getId(), 강남역.getId(), 역삼역.getId()));
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite(최현구.getEmail());

            // when
            deleteFavoriteService.deleteFavorite(loginMember, favorite.getId());

            // then
            assertThat(favoriteRepository.existsById(favorite.getId())).isFalse();
        }

        @DisplayName("즐겨찾기가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void deleteFavoriteWithNotExists() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite(최현구.getEmail());

            assertThatThrownBy(() -> deleteFavoriteService.deleteFavorite(loginMember, Long.MAX_VALUE))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ID 에 해당하는 즐겨찾기가 존재하지 않습니다. id: " + Long.MAX_VALUE);
        }

        @DisplayName("멤버 정보가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void deleteFavoriteWithInvalidMember() {
            Favorite favorite = favoriteRepository.save(new Favorite(최현구.getId(), 강남역.getId(), 역삼역.getId()));
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("abcde@naver.com");

            assertThatThrownBy(() -> deleteFavoriteService.deleteFavorite(loginMember, favorite.getId()))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + loginMember.getEmail());
        }

        @DisplayName("내가 등록하지 않은 즐겨 찾기를 제거 하려고 할 경우 예외가 발생한다.")
        @Test
        void deleteFavoriteWithNotMyFavorite() {
            Favorite favorite = favoriteRepository.save(new Favorite(Long.MAX_VALUE, 강남역.getId(), 역삼역.getId()));
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite(최현구.getEmail());

            assertThatThrownBy(() -> deleteFavoriteService.deleteFavorite(loginMember, favorite.getId()))
                    .isExactlyInstanceOf(IllegalStateException.class)
                    .hasMessage("즐겨찾기를 삭제할 권한이 없습니다.");
        }
    }
}
