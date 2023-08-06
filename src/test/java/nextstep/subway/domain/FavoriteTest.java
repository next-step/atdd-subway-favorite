package nextstep.subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    void isSameMember_pass() {
        Favorite favorite = new Favorite(1L, 1L, new Station(), new Station());
        assertTrue(favorite.isSameMember(1L));
    }

    @Test
    void isSameMember_fail() {
        Favorite favorite = new Favorite(1L, 1L, new Station(), new Station());
        assertTrue(favorite.isSameMember(2L));
    }



}