package subway.unit.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.SubwayBadRequestException;
import subway.member.domain.Favorite;
import subway.member.domain.Member;
import subway.member.domain.MemberFavorites;
import subway.member.domain.RoleType;
import subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberFavoritesTest {

    private Member member;
    private MemberFavorites memberFavorites;

    @BeforeEach
    void beforeEach() {
        final String email = "email@email.com";
        final String password = "password";
        final int age = 20;
        final RoleType role = RoleType.ROLE_MEMBER;
        member = Member.builder().age(age).password(password).email(email).role(role).build();
        memberFavorites = new MemberFavorites();
    }

    /**
     * 회원에
     * 즐겨찾기를 추가하면
     * 추가된다.
     */
    @DisplayName("즐겨찾기 추가 기능")
    @Test
    void add() {
        // TODO : 정리 하세요.

        //given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();
        Favorite build = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();

        memberFavorites.add(build, member);

        var favorites = memberFavorites.getFavorites();

        assertThat(favorites.size()).isEqualTo(1L);
    }

    /**
     * 중복된 즐겨찾기를 추가하면
     * 예외가 발생한다.
     */
    @DisplayName("즐겨찾기 추가 기능 : 중복 불가")
    @Test
    void addWithAlreadyFavorite() {
        // TODO : 정리 하세요.

        //given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();
        Favorite build = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        memberFavorites.add(build, member);

        assertThatThrownBy(() -> memberFavorites.add(build, member)).isInstanceOf(SubwayBadRequestException.class);
    }

    /**
     * 회원에
     * 즐겨찾기를 삭제하면
     * 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제 기능")
    @Test
    void remove() {
        // TODO : 정리 하세요.

        //given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();
        Favorite build = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        memberFavorites.add(build, member);

        memberFavorites.removeFavorite(build);

        assertThat(memberFavorites.getFavorites().size()).isEqualTo(0L);
//        assertThatThrownBy(() -> memberFavorites.add(build, member)).isInstanceOf(SubwayBadRequestException.class);
    }


}
