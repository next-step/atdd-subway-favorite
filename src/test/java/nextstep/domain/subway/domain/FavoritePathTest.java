package nextstep.domain.subway.domain;

import nextstep.domain.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FavoritePathTest {
    Station startStation;
    Station endStation;
    Member member;

    @BeforeEach
    void setUp() {
        startStation = new Station("A");
        endStation = new Station("B");
        member = new Member("E", "P", 20);
    }

    @DisplayName("FavoritePath 유효성 검사 - case 01")
    @Test
    void validCheck_case1() {
        //given
        startStation = null;
        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member);

        //when then
        assertThrows(IllegalArgumentException.class, favoritePath::validCheck);

    }
    @DisplayName("FavoritePath 유효성 검사 - case 02")
    @Test
    void validCheck_case2() {
        //given
        endStation = null;
        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member);

        //when
        assertThrows(IllegalArgumentException.class, favoritePath::validCheck);
    }
    @DisplayName("FavoritePath 유효성 검사 - case 03")
    @Test
    void validCheck_case3() {
        //given
        member = null;
        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member);

        //when
        assertThrows(IllegalArgumentException.class, favoritePath::validCheck);
    }

}