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

    @DisplayName("FavoritePath 유효성 검사 - 시작역 데이터가 null 인 경우")
    @Test
    void validCheck_case1() {
        //given
        startStation = null;

        //when then
        assertThrows(IllegalArgumentException.class, () -> new FavoritePath(startStation, endStation, member));

    }
    @DisplayName("FavoritePath 유효성 검사 - 종착역 데이터가 null 인 경우")
    @Test
    void validCheck_case2() {
        //given
        endStation = null;

        //when
        assertThrows(IllegalArgumentException.class, () -> new FavoritePath(startStation, endStation, member));
    }
    //Todo: 다시 생각해보니 member 는 아에 null 이면 이전에 걸릴거 같기는 한데... 이런 경우에도 테스트를 해야할 까요?
    @DisplayName("FavoritePath 유효성 검사 - 즐겨찾기 신청 멤버의 데이터가 null 인 경우")
    @Test
    void validCheck_case3() {
        //given
        member = null;

        //when
        assertThrows(IllegalArgumentException.class, () -> new FavoritePath(startStation, endStation, member));
    }

}