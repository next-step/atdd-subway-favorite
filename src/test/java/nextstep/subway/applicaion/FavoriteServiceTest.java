package nextstep.subway.applicaion;

import static nextstep.auth.token.acceptance.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("즐겨찾기 서비스 테스트")
@SpringBootTest
public class FavoriteServiceTest {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FavoriteService favoriteService;


    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        Long sourceId = stationRepository.save(new Station("강남역")).getId();
        Long targetId = stationRepository.save(new Station("신논현역")).getId();
        memberRepository.save(new Member(사용자1.getEmail(), "password", 20));

        Long favoriteId = favoriteService.createFavorite(사용자1.getEmail(), new FavoriteRequest(sourceId, targetId));

        assertThat(favoriteRepository.findById(favoriteId)).isNotNull();
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void findAll() {
        Station source1 = stationRepository.save(new Station("강남역"));
        Station target1 = stationRepository.save(new Station("신논현역"));
        Station source2 = stationRepository.save(new Station("역삼역"));
        Station target2 = stationRepository.save(new Station("판교역"));
        Member member = memberRepository.save(new Member(사용자1.getEmail(), "password", 20));
        Favorite favorite1 = favoriteRepository.save(
            new Favorite(member.getId(), source1, target1));
        Favorite favorite2 = favoriteRepository.save(
            new Favorite(member.getId(), source2, target2));

        List<FavoriteResponse> favorites = favoriteService.findAll(사용자1.getEmail());

        assertThat(favorites).hasSize(2);
        assertThat(favorites).containsExactly(
            new FavoriteResponse(
                favorite1.getId(),
                new StationResponse(source1.getId(), source1.getName()),
                new StationResponse(target1.getId(), target1.getName())
            ),
            new FavoriteResponse(
                favorite2.getId(),
                new StationResponse(source2.getId(), source2.getName()),
                new StationResponse(target2.getId(), target2.getName())
            )
        );
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete() {
        Station source = stationRepository.save(new Station("강남역"));
        Station target = stationRepository.save(new Station("신논현역"));
        Member member = memberRepository.save(new Member(사용자1.getEmail(), "password", 20));
        favoriteRepository.save(new Favorite(member.getId(), source, target));

        favoriteService.delete(사용자1.getEmail(), source, target);

        assertThat(favoriteRepository.findAll()).isEmpty();
    }

    @DisplayName("즐겨찾기 삭제 실패 - 즐겨찾기가 없는 경우")
    @Test
    void delete_fail_nonExistFavoriteId() {
        Station source = stationRepository.save(new Station("강남역"));
        Station target = stationRepository.save(new Station("신논현역"));
        Member member = memberRepository.save(new Member(사용자1.getEmail(), "password", 20));

        assertThatThrownBy(() -> favoriteService.delete(사용자1.getEmail(), source, target))
            .isInstanceOf(IllegalStateException.class);
    }
}
