package nextstep.favorite.domain;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    /**
     * given : target, source Station과 member를 받아서
     * when : Favorite 생성시
     * then : 객체가 생성된다.
     */
    @Test
    @DisplayName("생성 테스트")
    void FavoriteCreateTest() {
        // given
        Station target = new Station("target");
        Station source = new Station("source");
        Member member = new Member("test", "123", 10);

        // when
        Favorite favorite = new Favorite(target, source, member);

        // then
        assertNotNull(favorite);
    }


    /**
     * given : Favorite 생성 후
     * when : Favorite 를 만든 멤버인 경우
     * then : 만든 멤버인 경우에만 삭제가 가능하다
     */
    @Test
    @DisplayName("삭제 테스트")
    void FavoriteIsCreateUserTest() {
        // given
        Station target = new Station("target");
        Station source = new Station("source");
        Member member = new Member("test", "123", 10);

        Favorite favorite = new Favorite(target, source, member);

        // when
        boolean isCreateUser = favorite.isCreateUser(member);

        //then
        assertTrue(isCreateUser);
    }
}