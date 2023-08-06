package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    void isSameMember_pass() {
        Favorite favorite = new Favorite(1L, 1L, new Station(), new Station());
        assertThat(favorite.isSameMember(1L)).isTrue();
    }

    @Test
    void isSameMember_fail() {
        Favorite favorite = new Favorite(1L, 1L, new Station(), new Station());
        assertThat(favorite.isSameMember(2L)).isFalse();
    }



}