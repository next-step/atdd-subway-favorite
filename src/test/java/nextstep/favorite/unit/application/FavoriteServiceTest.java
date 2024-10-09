package nextstep.favorite.unit.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.transaction.Transactional;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.global.exception.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.section.SectionRepository;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FavoriteServiceTest {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    private Station 강남역;
    private Station 선릉역;
    private Member 랜덤유저;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        선릉역 = stationRepository.save(new Station("선릉역"));
        랜덤유저 = memberRepository.save(new Member("random@gmail.com", "password", 100));
        sectionRepository.save(new Section(강남역.getId(), 선릉역.getId(), 10));
    }

    @AfterEach
    void tearDown() {
        favoriteRepository.deleteAllInBatch();
        sectionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
    }

    @Test
    void 즐겨찾기를_생성한다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());

        // when
        favoriteService.createFavorite(loginMember, request);

        // then
        List<Favorite> results = favoriteRepository.findAll();
        assertThat(results.get(0).getMemberId()).isEqualTo(랜덤유저.getId());
        assertThat(results.get(0).getSourceId()).isEqualTo(강남역.getId());
        assertThat(results.get(0).getTargetId()).isEqualTo(선릉역.getId());
    }

    @Test
    void 이미_등록된_즐겨찾기는_다시_생성할_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        favoriteService.createFavorite(loginMember, request);

        // when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 등록된 즐겨찾기입니다.");
    }

    @Test
    void 존재하지_않는_역으로는_즐겨찾기를_생성할_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        Long wrongStationId = -1L;
        FavoriteRequest request = new FavoriteRequest(wrongStationId, 선릉역.getId());

        // when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 지하철입니다.");
    }

    @Test
    void 같은_역으로는_즐겨찾기를_생성할_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(선릉역.getId(), 선릉역.getId());

        // when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 종착역이 같습니다.");
    }

    @Test
    void 연결되지않은_역으로는_즐겨찾기를_생성할_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        Station 부산역 = stationRepository.save(new Station("부산역"));
        FavoriteRequest request = new FavoriteRequest(선릉역.getId(), 부산역.getId());

        // when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 종착역이 연결되어 있지 않습니다.");
    }

    @Test
    void 등록되지_않은_유저는_즐겨찾기를_생성할_수_없다() {
        // given
        String wrongEmail = "wrong@gmail.com";
        LoginMember loginMember = new LoginMember(wrongEmail);
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());

        // when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, request))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 즐겨찾기를_읽어온다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        favoriteService.createFavorite(loginMember, request);

        // when
        List<FavoriteResponse> results = favoriteService.findFavorites(loginMember);

        // then
        assertThat(results.get(0)).isNotNull();
        assertThat(results.get(0).getSource().getName()).isEqualTo(강남역.getName());
        assertThat(results.get(0).getTarget().getName()).isEqualTo(선릉역.getName());
    }

    @Test
    void 다른유저의_즐겨찾기를_읽어올_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        favoriteService.createFavorite(loginMember, request);

        memberRepository.save(new Member("wrong@gmail.com", "password", 1));
        LoginMember 다른유저 = new LoginMember("wrong@gmail.com");

        // when
        List<FavoriteResponse> results = favoriteService.findFavorites(다른유저);

        // then
        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    void 즐겨찾기를_삭제한다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        Long favoriteId = favoriteService.createFavorite(loginMember, request);

        // when
        favoriteService.deleteFavorite(loginMember, favoriteId);

        // then
        List<Favorite> results = favoriteRepository.findAll();
        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    void 다른유저의_즐겨찾기를_삭제할_수_없다() {
        // given
        LoginMember loginMember = new LoginMember("random@gmail.com");
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        Long favoriteId = favoriteService.createFavorite(loginMember, request);

        memberRepository.save(new Member("wrong@gmail.com", "password", 1));
        LoginMember 다른유저 = new LoginMember("wrong@gmail.com");

        // when && then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(다른유저, favoriteId))
                .isInstanceOf(AuthenticationException.class);

    }
}
