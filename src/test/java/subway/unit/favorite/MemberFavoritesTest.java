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

    @DisplayName("즐겨찾기 추가 기능")
    @Test
    void add() {
        // given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();

        // when
        Favorite favorite = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        memberFavorites.add(favorite, member);

        // then
        var favorites = memberFavorites.getFavorites();
        assertThat(favorites.size()).isEqualTo(1L);
    }

    @DisplayName("즐겨찾기 추가 기능 : 중복 불가")
    @Test
    void addWithAlreadyFavorite() {
        // given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();

        // when
        Favorite build = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        memberFavorites.add(build, member);

        // then
        assertThatThrownBy(() -> memberFavorites.add(build, member)).isInstanceOf(SubwayBadRequestException.class);
    }

    @DisplayName("즐겨찾기 삭제 기능")
    @Test
    void remove() {
        // given
        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();
        Favorite favorite = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        member.appendFavorite(favorite);

        // when
        member.deleteFavoriteByFavorite(favorite);

        // then
        assertThat(memberFavorites.getFavorites().size()).isEqualTo(0L);
    }

    @DisplayName("즐겨찾기 삭제 기능 : 다른 소유자의 즐겨찾기 삭제 불가")
    @Test
    void removeWithNotMyOwn(){
        // given
        final String email = "email2@email.com";
        final String password = "password2";
        final int age = 30;
        final RoleType role = RoleType.ROLE_MEMBER;
        Member anotherMember = Member.builder().age(age).password(password).email(email).role(role).build();


        Station 강남역 = Station.builder().name("강남역").build();
        Station 역삼역 = Station.builder().name("역삼역").build();
        Favorite favorite = Favorite.builder().member(member).sourceStation(강남역).targetStation(역삼역).build();
        member.appendFavorite(favorite);

        // when/then
        assertThatThrownBy(() -> anotherMember.deleteFavoriteByFavorite(favorite))
                .isInstanceOf(SubwayBadRequestException.class);
    }


}
