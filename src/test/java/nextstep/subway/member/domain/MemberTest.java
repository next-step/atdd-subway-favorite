package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    public static final String EMAIL = "dhlee@tst.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 11;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(EMAIL, PASSWORD, AGE);
    }

    @Test
    public void addFavoriteTest() {
        // given
        Favorite favorite = new Favorite(1L, 2L);

        // when
        member.addFavorite(favorite);

        // then
        List<Favorite> favorites = member.findAllFavorite();

        assertThat(favorites).contains(favorite);
    }

    @Test
    public void deleteFavorite() {
        // given

        // when

        // then

    }

}