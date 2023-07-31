package nextstep.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FavoriteRepositoryTest {

    public static final String TEST_EMAIL = "email@email.com";
    public static final int SOURCE = 1;
    public static final int TARGET = 3;
    @Autowired
    FavoriteRepository favoriteRepository;

    @DisplayName("즐겨 찾기를 저장하면 아이디를 부여한다")
    @Test
    void save() {
        // given
        Favorite favorite = new Favorite(TEST_EMAIL, SOURCE, TARGET);

        // when
        Favorite save = favoriteRepository.save(favorite);

        // then
        Assertions.assertAll(
                () -> assertThat(save.getId()).isEqualTo(SOURCE),
                () -> assertThat(save.getEmail()).isEqualTo(TEST_EMAIL),
                () -> assertThat(save.getSource()).isEqualTo(SOURCE),
                () -> assertThat(save.getTarget()).isEqualTo(TARGET)
        );
    }
}
