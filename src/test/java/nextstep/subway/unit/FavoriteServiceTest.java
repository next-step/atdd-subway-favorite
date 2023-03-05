package nextstep.subway.unit;

import nextstep.member.application.dto.MemberInfoVo;
import nextstep.member.application.dto.abstractive.MemberProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.applicaion.message.Message;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단위 테스트 : FavoriteService")
@Import({StationService.class, FavoriteService.class})
@DataJpaTest
public class FavoriteServiceTest {
    @Autowired
    StationRepository stationRepository;
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FavoriteService favoriteService;
    Station station1;
    Station station2;
    FavoriteCreateRequest request;
    MemberProvider memberProvider;

    @BeforeEach
    public void setUp() {
        station1 = stationRepository.save(new Station("강남역"));
        station2 = stationRepository.save(new Station("판교역"));
        Member member = memberRepository.save(new Member("member@email.com", "password", 1, List.of(RoleType.ROLE_MEMBER.name())));

        request = new FavoriteCreateRequest(station1.getId(), station2.getId());
        memberProvider = new MemberInfoVo(member.getEmail(), member.getRoles());
    }

    @Test
    @DisplayName("성공 : 역 즐겨찾기 생성")
    void create_favorite_success() {
        Favorite favorite = favoriteService.createFavorite(memberProvider, request);
        assertThat(favorite.getSource().getName()).isEqualTo(station1.getName());
        assertThat(favorite.getTarget().getName()).isEqualTo(station2.getName());
    }

    @Test
    @DisplayName("실패 : 역 즐겨찾기 생성 : source, target이 같은 경우")
    void create_favorite_fail() {
        FavoriteCreateRequest request = new FavoriteCreateRequest(station1.getId(), station1.getId());
        assertThatThrownBy(() -> {
            favoriteService.createFavorite(memberProvider, request);
        }).hasMessage(Message.SOURCE_TARGET_DUPLICATE_STATION.getMessage());
    }

    @Test
    @DisplayName("성공 : 역 즐겨찾기 조회")
    void read_favorites() {
        favoriteService.createFavorite(memberProvider, request);
        assertThat(favoriteService.readFavorites(memberProvider))
                .extracting(FavoriteReadResponse::getSource)
                .extracting(FavoriteReadResponse.FavoriteReadStationResponse::getName)
                .contains("강남역");
        assertThat(favoriteService.readFavorites(memberProvider))
                .extracting(FavoriteReadResponse::getTarget)
                .extracting(FavoriteReadResponse.FavoriteReadStationResponse::getName)
                .contains("판교역");
    }

    @Test
    @DisplayName("성공 : 역 삭제")
    void delete_favorite() {
        Favorite favorite = favoriteService.createFavorite(memberProvider, request);
        favoriteService.deleteFavorite(memberProvider, favorite.getId());
        assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
    }

}
