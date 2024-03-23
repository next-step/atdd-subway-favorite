package nextstep.favorite.unit;

import nextstep.favorite.application.AddFavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
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

@DisplayName("즐겨찾기 생성 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddFavoriteTest {

    @Autowired
    private AddFavoriteService addFavoriteService;

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

    @DisplayName("내 계정에 지하철 경로를 즐겨찾기 할 수 있다.")
    @Nested
    class AddFavorite {
        @DisplayName("즐겨찾기에 지하철역을 등록한다.")
        @Test
        void addFavorite() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("jinha3507@gmail.com");
            FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());

            addFavoriteService.addFavorite(loginMember, request);

            List<Favorite> results = favoriteRepository.findAll();
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getId()).isNotNull();
            assertThat(results.get(0).getMemberId()).isEqualTo(최현구.getId());
            assertThat(results.get(0).getSourceStationId()).isEqualTo(강남역.getId());
            assertThat(results.get(0).getTargetStationId()).isEqualTo(역삼역.getId());
        }

        @DisplayName("멤버 정보가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void addFavoriteWithInvalidMember() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("abcde@naver.com");
            FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());

            assertThatThrownBy(() -> addFavoriteService.addFavorite(loginMember, request))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + loginMember.getEmail());
        }

        @DisplayName("경로를 찾을 수 없거나 연결되지 않는 등 경로 조회가 불가능한 조회의 경우 즐겨찾기로 등록할 수 없다.")
        @Test
        void addFavoriteWithInvalidPath() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("jinha3507@gmail.com");
            FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());

            assertThatThrownBy(() -> addFavoriteService.addFavorite(loginMember, request))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("도착역이 존재하지 않습니다. stationId: " + 선릉역.getId());
        }

        @DisplayName("존재하지 않는 역을 즐겨찾기로 등록할 수 없다.")
        @Test
        void addFavoriteWithInvalidStation() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("jinha3507@gmail.com");
            FavoriteRequest request = new FavoriteRequest(강남역.getId(), Long.MAX_VALUE);

            assertThatThrownBy(() -> addFavoriteService.addFavorite(loginMember, request))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지하철 역이 존재하지 않습니다. stationId: " + Long.MAX_VALUE);
        }

        @DisplayName("즐겨찾기에 이미 등록된 역을 다시 등록할 수 없다.")
        @Test
        void addFavoriteWithDuplicateStation() {
            LoginMemberForFavorite loginMember = new LoginMemberForFavorite("jinha3507@gmail.com");
            FavoriteRequest request = new FavoriteRequest(강남역.getId(), 역삼역.getId());
            addFavoriteService.addFavorite(loginMember, request);

            assertThatThrownBy(() -> addFavoriteService.addFavorite(loginMember, request))
                    .isExactlyInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 즐겨찾기에 등록된 경로입니다. member: " + 최현구.getId() + ", source: " + 강남역.getId() + ", target: " + 역삼역.getId());
        }
    }
}
