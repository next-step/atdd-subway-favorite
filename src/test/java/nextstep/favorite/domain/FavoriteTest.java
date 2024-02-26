package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("즐겨찾기 도메인 테스트")
class FavoriteTest {

    private final String email = "test@email.com";
    private final String password = "password";
    private final int age = 20;

    private Member member;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        member = new Member(email, password, age);
        source = new Station("강남역");
        target = new Station("역삼역");
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {

        Favorite favorite = new Favorite(member, source, target);
        assertAll(
                () -> assertEquals(member, favorite.getMember()),
                () -> assertEquals(source, favorite.getSource()),
                () -> assertEquals(target, favorite.getTarget()),
                () -> assertTrue(favorite.isOwner(member))
        );
    }

}
