package nextstep.favorite.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @Test
    void owner맞음() {
        final Favorite favorite = new Favorite(1L, null, null);
        assertThat(favorite.isOwner(1L)).isTrue();
    }

    @Test
    void owner아님() {
        final Favorite favorite = new Favorite(1L, null, null);
        assertThat(favorite.isOwner(-1L)).isTrue();
    }
}