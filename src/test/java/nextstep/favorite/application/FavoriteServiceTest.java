package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.dto.CreateFavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        CreateFavoriteRequest request = createFavoriteRequest();

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
        CreateFavoriteRequest request = createFavoriteRequest();
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

    private CreateFavoriteRequest createFavoriteRequest() {
        return new CreateFavoriteRequest(source.getId(), target.getId());
    }
}