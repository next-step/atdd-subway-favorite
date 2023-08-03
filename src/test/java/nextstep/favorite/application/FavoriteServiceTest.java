package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@DisplayName("즐겨찾기 서비스 테스트")
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    private Member member;
    private Station source, target;
    @BeforeEach
    void setUp() {
        member = memberRepository.save(createMember());
        source = createStation("강남역");
        target = createStation("역삼역");
        Line line = createLine();
        line.addSection(source, target, 10);
        lineRepository.save(line);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // given : 선행조건 기술
        UserPrincipal userPrincipal = createUserPrincipal();
        CreateFavoriteRequest request = createFavoriteRequest(source.getId(), target.getId());

        // when : 기능 수행
        FavoriteResponse response = favoriteService.createFavorite(userPrincipal, request);

        // then : 결과 확인
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getSource().getName()).isEqualTo("강남역");
        assertThat(response.getTarget().getName()).isEqualTo("역삼역");
        assertThat(response.getSource().getId()).isEqualTo(source.getId());
        assertThat(response.getTarget().getId()).isEqualTo(target.getId());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void findFavorites() {
        // given : 선행조건 기술
        UserPrincipal userPrincipal = createUserPrincipal();
        CreateFavoriteRequest request = createFavoriteRequest(source.getId(), target.getId());
        favoriteService.createFavorite(userPrincipal, request);

        // when : 기능 수행
        List<FavoriteResponse> favorites = favoriteService.findFavorites();

        // then : 결과 확인
        assertThat(favorites).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
        assertThat(favorites).hasSize(1)
                .extracting("source.name", "target.name")
                .containsExactly(tuple("강남역", "역삼역"));
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given : 선행조건 기술
        UserPrincipal userPrincipal = createUserPrincipal();
        CreateFavoriteRequest request = createFavoriteRequest(source.getId(), target.getId());
        FavoriteResponse response = favoriteService.createFavorite(userPrincipal, request);

        // when : 기능 수행
        favoriteService.deleteFavorite(userPrincipal, response.getId());

        // then : 결과 확인
        List<FavoriteResponse> favorites = favoriteService.findFavorites();
        assertThat(favorites).isEmpty();
    }

    @DisplayName("비 정상적인 즐겨찾기 추가시(출발역과 도착역이 같다) 예외를 던진다.")
    @Test
    void createFavoriteReturnException() {
        // given : 선행조건 기술
        UserPrincipal userPrincipal = createUserPrincipal();
        CreateFavoriteRequest request = createFavoriteRequest(source.getId(), source.getId());

        // when : 기능 수행 then : 결과 확인
        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비 정상적인 즐겨찾기 추가시(경로가 없는 경우) 예외를 던진다.")
    @Test
    void createFavoriteReturnException2() {
        // given : 선행조건 기술
        Long 교대역 = 기존_구간과_다른_구간생성();
        UserPrincipal userPrincipal = createUserPrincipal();
        CreateFavoriteRequest request = createFavoriteRequest(교대역, source.getId());

        // when : 기능 수행 then : 결과 확인
        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Long 기존_구간과_다른_구간생성() {
        Station 교대역 = createStation("교대역");
        Station 이수역 = createStation("이수역");
        Line line = createLine();
        line.addSection(교대역, 이수역, 10);
        lineRepository.save(line);
        return 교대역.getId();
    }
    private Member createMember() {
        return new Member(
                "email@email.com",
                "password",
                20,
                "ROLE_MEMBER"
        );
    }

    private Line createLine() {
        return new Line("2호선", "green");
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private UserPrincipal createUserPrincipal() {
        return new UserPrincipal(member.getEmail(), member.getPassword());
    }

    private CreateFavoriteRequest createFavoriteRequest(Long sourceId, Long targetId) {
        return new CreateFavoriteRequest(sourceId, targetId);
    }
}