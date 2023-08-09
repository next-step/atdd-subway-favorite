package nextstep.favorite.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.exception.AuthenticationException;
import nextstep.exception.ShortPathSameStationException;
import nextstep.exception.StationNotExistException;
import nextstep.favorite.application.request.FavoriteCreateRequest;
import nextstep.favorite.application.response.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.line.LineTestField.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    private Station gangnamStation;
    private Station seolleungStation;
    private Station suwonStation;
    private Station nowonStation;
    private Station dearimStation;

    private Member member;
    private Member defferentMember;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        gangnamStation = saveStation(GANGNAM_STATION_NAME);
        seolleungStation = saveStation(SEOLLEUNG_STATION_NAME);
        suwonStation = saveStation(SUWON_STATION_NAME);
        nowonStation = saveStation(NOWON_STATION_NAME);
        dearimStation = saveStation(DEARIM_STATION_NAME);
        member = saveMember("email", "password", 15, "role");
        defferentMember = saveMember("email2", "password2", 20, "role");
        saveLine(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2);
        saveLine(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3);
        saveLine(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5);
    }

    @DisplayName("경로가 정상일경우 즐겨찾기가 등록된다.")
    @Test
    void createFavorite() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, nowonStation);

        // when
        Favorite favorite = favoriteService.createFavorite(userPrincipal, favoriteCreateRequest);

        // then
        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getSource()).isEqualTo(gangnamStation);
        assertThat(favorite.getTarget()).isEqualTo(nowonStation);
    }

    @DisplayName("경로에 포함되지 않은 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void createFavorite_fail_not_exist_station_in_line() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, dearimStation);

        // when then
        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, favoriteCreateRequest))
                .isExactlyInstanceOf(StationNotExistException.class)
                .hasMessage("노선에 역이 존재하지 않습니다.");
    }

    @DisplayName("동일한 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void createFavorite_fail_source_target_same() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, gangnamStation);

        // when then
        assertThatThrownBy(() -> favoriteService.createFavorite(userPrincipal, favoriteCreateRequest))
                .isExactlyInstanceOf(ShortPathSameStationException.class)
                .hasMessage("최단경로 시작역, 종착역이 동일할 수 없습니다.");
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 조회시 해당 경로가 조회되야 한다.")
    @Test
    void findFavorites() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, nowonStation);
        Favorite favorite = favoriteService.createFavorite(userPrincipal, favoriteCreateRequest);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(userPrincipal);

        // then
        assertThat(favoriteResponses).hasSize(1).extracting("id")
                .containsExactly(favorite.getId());

        assertThat(favoriteResponses).extracting("source").extracting("name")
                .containsExactly(GANGNAM_STATION_NAME);

        assertThat(favoriteResponses).extracting("target").extracting("name")
                .containsExactly(NOWON_STATION_NAME);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 해당 경로가 삭제되야 한다.")
    @Test
    void deleteFavorite() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, nowonStation);
        Favorite favorite = favoriteService.createFavorite(userPrincipal, favoriteCreateRequest);

        // when
        favoriteService.deleteFavorite(favorite.getId(), userPrincipal);

        // then
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(userPrincipal);
        assertThat(favoriteResponses).hasSize(0);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 다른 사용자일경우 에러를 던진다.")
    @Test
    void deleteFavorite_not_owner() {
        // given
        UserPrincipal userPrincipal = createUserPrinCipal(member);
        UserPrincipal differentUserPrincipal = createUserPrinCipal(defferentMember);
        FavoriteCreateRequest favoriteCreateRequest = createFavoriteCreateRequest(gangnamStation, nowonStation);
        Favorite favorite = favoriteService.createFavorite(userPrincipal, favoriteCreateRequest);

        // when then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(favorite.getId(), differentUserPrincipal))
                .isExactlyInstanceOf(AuthenticationException.class);
    }

    private UserPrincipal createUserPrinCipal(Member member) {
        return new UserPrincipal(member.getEmail(), member.getRole());
    }

    private FavoriteCreateRequest createFavoriteCreateRequest(Station source, Station target) {
        return new FavoriteCreateRequest(source.getId(), target.getId());
    }

    private Station saveStation(String stationName) {
        return stationRepository.save(new Station(stationName));
    }

    private Line saveLine(String name, String color, Station upStation, Station downStation, int distance) {
        return lineRepository.save(new Line(name, color, upStation, downStation, distance));
    }

    private Member saveMember(String email, String password, int age, String role) {
        return memberRepository.save(new Member(email, password, age, role));
    }

}
